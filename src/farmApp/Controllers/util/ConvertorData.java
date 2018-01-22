
package farmApp.Controllers.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;






public class ConvertorData {

    
    
    public Timestamp convertToDatabaseColumn(LocalDateTime ldt) {
       
          
       return Timestamp.valueOf(ldt);
       

    }

    
    public LocalDateTime convertToEntityAttribute(Timestamp ts) {

        
        if (ts != null) {
            return ts.toLocalDateTime();
        }
        return null;

    }

   
}


// implements AttributeConverter<LocalDateTime, Timestamp>