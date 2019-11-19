

package com.github.huzekang.jdbcservice.model;

import com.github.huzekang.jdbcservice.enums.UploadModeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
@NotNull(message = "upload info cannot be null")
public class SourceDataUpload {

    @NotBlank(message = "uplaod table name cannot be EMPTY")
    private String tableName;

    private String primaryKeys;

    private String indexKeys;

    private Short mode = UploadModeEnum.NEW.getMode();


    public List<Map<String, String>> getIndexList() {
        List<Map<String, String>> indexs = null;
        if (null != indexKeys) {
            String[] idxs = indexKeys.split(",");
            indexs = new ArrayList<>();
            for (String idx : idxs) {
                Map<String, String> map = new HashMap<>();
                map.put("INDEX_" + idx.toUpperCase(), idx);
                indexs.add(map);
            }
        }
        return indexs;
    }

}
