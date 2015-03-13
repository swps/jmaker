Toolkits and libraries to push and pull data in and out of FileMaker in Java leveraging existing technologies such as JaxB and Freemarker.

FileMaker exposes its data via XML REST APIs.  Using these APIs, jMaker connects to FileMaker Pro installations and introspects what databases are available over the web, and generates custom classes for querying and updating YOUR FileMaker databases through exposed Layouts.

For example, to get Product data from FileMaker with an id of 5001:


> FileMakerServerConfiguration fmc = FileMakerServerConfiguration.createInstance();

> ProductQuery prodQuery = new ProductQuery(fmc);
> prodQuery.setId(5001);

> Collection

&lt;ProductRecord&gt;

 prodRecords = prodQuery.find();
> for ( ProductRecord pRecord : prodRecords )
> {
> > //... whatever you gotta do
> > int recnum = pRecord.getFileMakerRecNum();

> }


Or similarly to update a FileMaker record once you know the recNum:

> FileMakerServerConfiguration fmc = FileMakerServerConfiguration.createInstance();
> ProductQuery prodQuery = new ProductQuery(fmc);

> prodQuery.setName('Gremlins');
> //get recnum from a found set as above
> ProductRecord fmProductUpdate = prodQuery.edit(recnum);

Where ProductRecord and ProductQuery are generated files from our ant task compiled into a library built just for you!

Note: As of today, the code generated pretty much only handles fields that are NUMBER (Double) and TEXT (String).  Also, as this project is in its infancy, FileMaker columns that have illegal Java characters aren't handled  correctely just yet.