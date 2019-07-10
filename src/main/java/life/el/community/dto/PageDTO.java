package life.el.community.dto;

import life.el.community.model.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageDTO {
    private List<QuestionDTO> data;
    //向前一页
    private boolean showPrevious;
    //回到首页
    private boolean showFirstPage;
    //向后一页
    private boolean showNext;
    //回到尾页
    private boolean showEndPage;
    //当前页面
    private Integer page;
    //当前可选的页面数组
    private List<Integer> pages = new ArrayList<>();
    //总页数
    private Integer totalPage;

    public void setPagination(Integer totalPage, Integer page) {

        this.totalPage = totalPage;
        this.page = page;

        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);
            }
            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }
        //如果是第一页，则没有向上一页&回到首页按钮
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }
        //如果是最后一页，则没有向后一页&回到尾页按钮
        if (page == totalPage) {
            showNext = false;

        } else {
            showNext = true;
        }
        //如果展示的页面列表中包含首页，则没有回到首页按钮
        if (pages.contains(1)) {
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }
        //如果展示的页面列表中包含尾页，则没有回到尾页按钮
        if (pages.contains(totalPage)) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }
    }
}
