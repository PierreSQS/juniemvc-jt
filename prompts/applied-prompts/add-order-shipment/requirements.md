## Change Requirements
Add a new entity to the project called BeerOrderShipment. 

The BeerOrderShipment entity has the following properties:
* shipmentDate - not null
* carrier
* trackingNumber

The BeerOrderShipment entity should extend the BaseEntity. The BeerOrder entity has a OneToMany relationship BeerOrderShipment.

Add a flyway migration script for the new BeerOrderShipment JPA Entity.

The path for controller operations should be "/api/v1/beer-orders/{beerOrderId}", where the beerOrderId is the id 
of the owning BeerOrder entity.

The controller and service will need the id of the BeerOrder the BeerOrderShipment belongs to.

Add Java DTOs, Mappers, Spring Data Repositories, service and service implementation to support a Spring MVC RESTful 
CRUD controller. Add Tests for all components. Update the OpenAPI documentation for the new controller operations. Verify 
all tests are passing.