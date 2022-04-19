package life.majiang.community.cache;

import life.majiang.community.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by codedrinker on 2019/6/5.
 */
public class TagCache {
    public static List<TagDTO> get() {
        List<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("考研初试");
        program.setTags(Arrays.asList("上岸经验分享", "失败教训分享","一战", "二战", "三战", "多战", "985", "211", "双一流", "普通院校", "高分政治", "高分英语一", "高级英语二", "高分数学一", "高分数学二", "高分数学三", "高分专业课", "学硕", "专硕", "择校推荐", "教育类", "管综", "法律类", "计算机类", "翻译类", "心理类","文科", "理科", "工科"));
        tagDTOS.add(program);

        TagDTO program2 = new TagDTO();
        program2.setCategoryName("考研复试");
        program2.setTags(Arrays.asList("复试过程", "英语口语", "自我介绍", "专业问题", "复试超车", "复试翻车", "联系导师"));
        tagDTOS.add(program2);

        TagDTO framework = new TagDTO();
        framework.setCategoryName("考公");
        framework.setTags(Arrays.asList("行政职业能力测验","申论","国考", "北京省考", "天津省考", "上海省考", "重庆省考", "内蒙古省考", "广西省考", "西藏省考", "宁夏省考", "新疆省考","河北省考","山西省考","辽宁省考","吉林省考","黑龙江省考","江苏省考","浙江省考","安徽省考","福建省考","江西省考","山东省考","河南省考","湖北省考","湖南省考","广东省考","海南省考","四川省考","贵州省考","云南省考","陕西省考","甘肃省考","青海省考","台湾省考","香港省考","澳门省考"));
        tagDTOS.add(framework);


        TagDTO server = new TagDTO();
        server.setCategoryName("就业");
        server.setTags(Arrays.asList("销售型行业", "服务型行业","工科行业", "艺术行业","文科行业","理科行业","医学行业","教育行业","计算机",  "通信", "警察", "消防", "军人", "会计", "媒体", "导游"));
        tagDTOS.add(server);


        return tagDTOS;
    }

    public static String filterInvalid(String tags) {
        String[] split = StringUtils.split(tags, ",");
        List<TagDTO> tagDTOS = get();

        List<String> tagList = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> StringUtils.isBlank(t) || !tagList.contains(t)).collect(Collectors.joining(","));
        return invalid;
    }

    public static void main(String[] args) {
        int i = (5 - 1) >>> 1;
        System.out.println(i);
    }
}
