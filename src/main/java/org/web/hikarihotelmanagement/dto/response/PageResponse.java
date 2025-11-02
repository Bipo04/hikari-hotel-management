package org.web.hikarihotelmanagement.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Response phân trang")
public record PageResponse<T>(
        @Schema(description = "Danh sách dữ liệu")
        List<T> content,

        @Schema(description = "Số trang hiện tại (bắt đầu từ 0)", example = "0")
        int pageNumber,

        @Schema(description = "Số lượng phần tử mỗi trang", example = "10")
        int pageSize,

        @Schema(description = "Tổng số phần tử", example = "100")
        long totalElements,

        @Schema(description = "Tổng số trang", example = "10")
        int totalPages,

        @Schema(description = "Có phải trang đầu tiên không", example = "true")
        boolean first,

        @Schema(description = "Có phải trang cuối cùng không", example = "false")
        boolean last
) {}
