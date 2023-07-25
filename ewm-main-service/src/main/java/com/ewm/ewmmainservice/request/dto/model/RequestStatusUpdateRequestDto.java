package com.ewm.ewmmainservice.request.dto.model;

import com.ewm.ewmmainservice.request.model.NewRequestStatusUpdate;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestStatusUpdateRequestDto {
    private List<Long> requestIds;
    private NewRequestStatusUpdate status;
}
