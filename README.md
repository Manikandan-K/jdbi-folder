JDBI Folders and Mappers:
----------------------------------------

Usage
----------------------------------------
 Include this library in dependency list
 
       <dependency>
            <groupId>com.github.rkmk</groupId>
            <artifactId>jdbi-folder</artifactId>
            <version>1.0</version>
        </dependency>


Folder:
----------------------

  JDBI doesn't give the provision to automatically map results to  "one to many" relationship in java objects. We need to write the folder for all classes wherever we have "one to many" relationship. If we have more than two level of parent child relation, lets say entity A has many B entity and B has many C entity, folder logic will be cumbersome. Either we need to write the complex folder logic or fire multiple queries and mash up the values in java. 
  
  Can we have hibernate like annotation in jdbi ? Can we use those annotations while fetching results ?
  The answer is 'Yes'. Using this library, we can use hibernate like annotations.
  
Example: 

  Lets say we have Movie, Song, Director. Movie has many songs and each movie has one director.
  
      public class Movie {
        @PrimaryKey
        private Integer id;
        private String name;
    
        @OneToMany("song")
        private List<Song> songs;
    
        @OneToOne("director")
        private Director director;
      }

      public class Song  {
          @PrimaryKey
          private Integer id;
          private String name;
          private Integer movieId;
      }


      public class Director {
          @PrimaryKey
          private Integer id;
          private String name;
          private Integer movieId;
      }

We need to say which fields are primary keys by the annotation @PrimaryKey. This will be used for folding the objects.  If you see annotation we have specified value for "name". It's used to identify fields from the result set. We can use single query to fetch all these information. Just instantiate default folder.

Fluent Queries:
--------------------------

    GenericFolder folder = new GenericFolder(Movie.class);

    handle
    .createQuery("select
        m.*,
        s.id AS song$id,
        s.name AS song$name,
        d.id  AS director$id,
        d.name AS director$name
      from movie m
      left join song s      using (movie_id)
      left join director d  using (movie_id)
    ")
    .fold(folder.getAccumulator(), folder).
    

That's all. We can use this GenericFolder for any class. One catch is, we need to specify "namespace" in the query. ie, We have specified "song" as name in the oneToMany annotion. We need to use the same namespace "song" in the result field name followed by $.
For example to accesss the id of the song, We need to say song$id. We can use this folder for any level of relationship.

Sql Object:
--------------------------

    interface Dao {
        @SqlQuery("select
                m.*,
                s.id AS song$id,
                s.name AS song$name,
                d.id  AS director$id,
                d.name AS director$name
              from movie m
              join song s      using (movie_id)
              join director d  using (movie_id)
            ")
        private FoldingList<Movie> getMovies();    
    }
    
    FoldingList<Movie> foldedResult = dao.getMovies();
    List<Movie> movies =  foldedResult.getValues();
    
Whenever we need results to be folded, we need to use FoldingList<?> instead of List<?>. Result will be folded. Just call getValues on FoldingList will give List<?> values.
And register CustomMapperFactory & FoldingListContainerFactory to dbi.

       dbi.registerMapper(new CustomMapperFactory());
       dbi.registerContainerFactory(new FoldingListContainerFactory());

    

Mapper:
------------------------

  JDBI gives default mapper which maps the result to Java objects. It has some restriction.
  
    * It didn't automatically convert the underscore names to camel case field name in java object. By convention we use underscore names in database tables, and camelcase names in java object. 
    * It won't handle all the object types. (example sql array).
    
  So we end up writing mappers for every class.  We can git rid of all the mappers by using this library. It will automatically convert underscore names to camel case names. It also has annotation, @ColumnName("name"), by which we can specify the table column name. It also handles Sql arrays. And more importantly, we don't need to specify the @MapResultAsBean & @RegisterMapper in every method. We can configure at single place. Just register our factory to dbi.
    
    dbi.registerMapper(new CustomMapperFactory());
    
  Thats all. We can forget about the mappers. If we have very specific case for mapping any class, we can use @Mapper to override the default mapper.     
  By default, this mapper will be used for all the classes  And also if we want have any special case while mapping fields, we can do that also. We need to implement FieldMapperFactory<T>. Lets say we have wrapper object called Money which has currency value.
  
    public class MoneyMapperFactory implements FieldMapperFactory<Money>{
  
      @Override
      public Money getValue(ResultSet rs, int index) throws SQLException {
          return new Money(rs.getBigDecimal(index));
      }
  
      @Override
      public Boolean accepts(Class<?> type) {
          return type.isAssignableFrom(Money.class);
      }
    }

We just need to register this class to CustomMapperFactory. It will take care of converting value to Money Object.

    CustomMapperFactory customMapperFactory = new CustomMapperFactory();
    customMapperFactory.register( new MoneyMapperFactory());
    dbi.registerMapper(customMapperFactory);

We can extend this to handle JSON objects and other complex types.
Another use case would be having OneToOne relation inside another class. DefaultMapper will take care of mapping these objects.

    class Movie {
        private Integer id;
        private String name;
        
        @OneToOne("director")
        Director director;
    }

    interface Dao {
        @SqlQuery("select
                m.*,
                d.id  AS director$id,
                d.name AS director$name
              from movie m
              join director d  using (movie_id)
            ")
        private Movie getMovie();    
    }
    
    Movie movie = dao.getMovie();

Automatically Director object will be created inside Movie object using this result set.   

Version:
--------------------------------
1.0



  
    
    
    
    
    






  
  
