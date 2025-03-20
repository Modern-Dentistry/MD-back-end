package com.rustam.modern_dentistry.dto.response.create;

import com.rustam.modern_dentistry.dao.entity.Examination;
import com.rustam.modern_dentistry.dao.entity.teeth.Teeth;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeethExaminationResponse {

    Long id;

    Teeth teeth;

    Examination examination;
}
