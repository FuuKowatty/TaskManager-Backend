package pl.bartoszmech.application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Builder
public record CreateAndUpdateTaskRequestDto(
        @NotNull(message = "{task.title.required}")
        @NotBlank(message = "{task.title.not.blank}")
        @Size(min = 3, message = "{task.title.too.short}")
        @Size(max = 255, message = "{task.title.too.long}")
        String title,
        @NotNull(message = "{task.description.required}")
        @NotBlank(message = "{task.description.not.blank}")
        @Size(min = 3, message = "{task.description.too.short}")
        @Size(max = 255, message = "{task.description.too.long}")
        String description,
        @NotNull(message = "{task.endDate.required}")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm:ssZ", iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime endDate,
        @NotNull(message = "{task.assignedTo.required}")
        @Positive(message = "{task.assignedTo.positive}")
        Long assignedTo
) {
}
