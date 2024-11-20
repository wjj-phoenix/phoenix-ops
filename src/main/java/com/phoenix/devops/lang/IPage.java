package com.phoenix.devops.lang;

import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IPage<T> {
    @Parameter(description = "当前页码")
    private long page;

    @Parameter(description = "每页数据量大小")
    private long size;

    @Parameter(description = "总页码")
    private long pages;

    @Parameter(description = "总的数据量")
    private long total;

    @Parameter(description = "数据")
    private List<T> rows;

    public IPage(Page<T> page) {
        this.page = page.getPageNumber();
        this.size = page.getPageSize();
        this.pages = page.getTotalPage();
        this.total = page.getTotalRow();
        this.rows = page.getRecords();
    }

    /**
     * 根据完整的项目列表创建假分页。
     *
     * @param list     完整的项目列表，用于分页。
     * @param pageNum  要检索的页码。
     * @param pageSize  每页的项目数目。
     * @return 返回指定页面的Page对象，包含对应的项目列表。
     */
    public static <T extends Serializable> IPage<T> startPage(List<T> list, int pageNum, int pageSize) {
        int totalItems = list.size();
        int totalPages = (int)Math.ceil((double)pageNum / pageSize);

        // 计算请求页面上第一个项目的索引
        int startIndex = (pageNum - 1) * pageSize;
        // 确保不会超出列表范围
        startIndex = Math.max(startIndex, 0);

        // 计算请求页面上最后一个项目的索引
        int endIndex = startIndex + pageSize;
        // 确保不会超出列表范围
        endIndex = Math.min(endIndex, totalItems);

        // 从完整列表中提取当前页面的子列表
        List<T> pageItems = list.subList(startIndex, endIndex);

        // 创建并填充Page对象
        IPage<T> page = new IPage<>();
        page.setPage(pageNum);
        page.setSize(pageSize);
        page.setTotal(totalItems);
        page.setPages(totalPages);
        page.setRows(pageItems);
        return page;
    }
}
