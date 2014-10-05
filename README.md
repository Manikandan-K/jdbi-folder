JDBI Folders and Mappers:
----------------------------------------

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
    
        @OneToMany(name = "song", type = Song.class)
        private List<Song> songs;
    
        @OneToOne(name = "director")
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

That's all. We can use this GenericFolder for any class. One catch is, we need to specify "name" in the query. ie, We have specified "song" as name in the oneToMany annotion. We need to use the same name "song" in the result field name followed by $.
For example to accesss the id of the song, We need to say song$id. We can use this folder for any level of relationship.

Mapper:
------------------------

  JDBI gives default mapper which maps the result to Java objects. It has some restriction.
  
    * It didn't automatically convert the underscore names to camel case field name in java object. By convention we use underscore names in database tables, and camelcase names in java object. 
    * It won't handle all the object types. (example sql array).
    
  So we end up writing mappers for every class.  We can git rid of all the mappers by using this library. It will automatically convert underscore names to camel case names. It also has annotation, @ColumnName("name"), by which we can specify the table column name. It also handles Sql arrays. And more importantly, we don't need to specify the @MapResultAsBean & @RegisterMapper in every method. We can configure at single place. Just register our factory to dbi.
    
    dbi.registerMapper(new CustomMapperFactory());
    
  Thats all. We can forget about the mappers. If we have very specific case for mapping any class, we can exclude that class and have custom mapper for that class alone. Lets say we want to use this mapper for all the classes except Product class.    
  
    dbi.registerMapper(new CustomMapperFactory(Product.class));
    
  By default, this mapper will be used for all the classes unless we specify some classes in the excluded list like we did above. And also if we want have any special case while mapping fields, we can do that also. We need to implement FieldMapperFactory<T>. Lets say we want to have the precison of all BigDecimal to 5.
  
    public class BigDecimalMapperFactory implements FieldMapperFactory<BigDecimal>{
  
      @Override
      public BigDecimal getValue(ResultSet rs, int index) throws SQLException {
          return rs.getBigDecimal(index).setScale(5, RoundingMode.UP);
      }
  
      @Override
      public Boolean accepts(Class<?> type) {
          return type.isAssignableFrom(BigDecimal.class);
      }
    }

We just need to register this class to CustomMapperFactory.

    CustomMapperFactory customMapperFactory = new CustomMapperFactory();
    customMapperFactory.register( new BigDecimalMapperFactory());
    dbi.registerMapper(customMapperFactory);

We can extend this to handle JSON objects and other complex types.


Version:
--------------------------------
1.0

Installation:
--------------------------------
will be available in maven central soon.


  
    
    
    
    
    






  
  
