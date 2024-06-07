
package com.esprit.pfe.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mail {
    private String to;
    private String subject;
    private String body;
}
