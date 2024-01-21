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

        @NotNull(message = "Task title is required.")
        @NotBlank(message = "Task title must not be blank.")
        @Size(min = 3, message = "Task title must be at least 3 characters long.")
        @Size(max = 255, message = "Task title must not exceed 255 characters.")
        String title,
        @NotNull(message = "Task description is required.")
        @NotBlank(message = "Task description must not be blank.")
        @Size(min = 3, message = "Task description must be at least 3 characters long.")
        @Size(max = 255, message = "Task description must not exceed 255 characters.")
        String description,
        @NotNull(message = "Task end date is required.")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm:ssZ", iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime endDate,
        @NotNull(message = "Task assigned to is required.")
        @Positive(message = "Task assigned to must be a positive number.")
        Long assignedTo

) {
}
