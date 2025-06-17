Update the BeerController to use DTOs. In the package `models`, create a new POJO called BeerDto with the same properties 
as the JPA Entity Beer. The DTO should use annotations from Project Lombok, including Builder. Create a Mapstruct mapper 
to convert to and from the DTO. Mappers should be added to the package `mappers`. When converting from a DTO to the JPA 
entity ignore the properties for id, createDate and updateDate. Convert the service layer to accept DTO objects and to 
use the Mapstruct mapper for type conversions. Update the controller methods to use the new DTO pojo for input and access
to service methods. 