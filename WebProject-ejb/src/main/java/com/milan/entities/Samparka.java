
import dtos.ContactDto;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dell
 */
@Entity

@Table(name = "contact")
//@SqlResultSetMapping(
//  name = "ContactDtoMapping",
//    classes = @ConstructorResult(
//        targetClass =ContactDto.class,
//        columns = {
//            @ColumnResult(name = "name", type = String.class),
//            @ColumnResult(name = "address", type = String.class)
//        }
//    )
//)
//@NamedNativeQuery(
//    name =  "Samparka.getContactDto",
//    query = "SELECT name, address FROM contact",
//    resultSetMapping = "ContactDtoMapping"
//)

public class Samparka {
    
    @Id
    private Long id;
    
    private String name;
    private String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
}
