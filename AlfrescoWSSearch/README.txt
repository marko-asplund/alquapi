
Introduction
------------

The Alfresco Web Service Java client library makes it possible to access an Alfresco content
repository remotely via Web Service interface from Java based software.
The WS Java client does not provide string typing and returns results as lists of string-typed
key value pairs.

This class library provides a convenience layer on top of the Alfresco WS Java client.
It's a query execution and simple object-content mapping framework that maps query results to Java objects
and thus makes it easier for the developer to fetch data from an Alfresco repository while using strong typing.
The library will execute queries and maps content model types to Java types. The mappings are currently
defined in terms of Java code by a developer. Content types can be Alfresco's standard cm:content or
any subtype, including any custom content subtypes. Query criteria can also include custom content model
properties.

Users can define mappings for standard and custom content types.
The class library defines a mapping for the cm:content content type out of the box. A type mapping
associates an Alfresco content type to a Java class used for representing that content type as well as
a mapper class that is used to map document properties to Java object properties.
Type mappings are inherited based on Alfresco's content type hierarchy when no explicit mapping is
defined for a type. For example if there's no explicit mapping for a cm:content subtype my:content,
the mapping for cm:content will be used instead if one exists.

You can define mappings for only those content types which you need to use in your program code.
With the default mapping all documents of type cm:content or it's subtype are mapped to the CMContent Java type
and only properties defined for cm:content will be available. 

Limitations
- content types are currently limited to cm:content and its subtypes
- only query and get operations are supported e.g. content can't be stored in repository via this API


Demo code
---------

Code demonstrating how to execute queries and map your custom content model to Java types
can be found in the com.ixonos.alfresco.search.demo package. The package includes the following
classes:
- custom content model types: MotorVehicle, Car
- custom content model type mappers: MotorVehicleMapper, CarMapper
- custom content model query criteria: MotorVehicleSearchCriteria
- custom content model constants: MotorVehicleConstants
- search client: VehicleSearcher


Custom content model
--------------------
A custom content model can be found in demo/searchmodel.
To build the Alfresco module run the following commands
- cd demo/searchmodel
- ant

The AMP module can be found under build/dist.
Install the module in your Alfresco installation and create a few
documents of the motorVehicle, car and motorcycle.


Software requirements
---------------------
- Alfresco 2.1
- Java SE 6
- Apache Ant 1.7.0 (for building the custom demo content model AMP package)
- Eclipse 3.3.1 (used for development and building)
