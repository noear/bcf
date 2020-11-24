<detail>
    <form>
        <table>
            <tr style="display: none">
                <th>R_PGID：</th>
                <td><input type="hidden" id="r_pgid" value=""/></td>
            </tr>
            <tr>
                <th>PGID：</th>
                <td><input type="text" id="pgid" disabled="disabled" style="background-color: gainsboro"/></td>
            </tr>
            <tr>
                <th class="t2">P_PGID：</th>
                <td><input type="text" id="p_pgid" value=""/></td>
            </tr>
            <tr>
                <th>PG_Code：</th>
                <td>
                    <input type="text" id="pg_code">
                </td>
            </tr>
            <tr>
                <th class="t1">Tags：</th>
                <td><input type="text" id="tags"/></td>
            </tr>
            <tr>
                <th></th>
                <td></td>
            </tr>
            <tr>
                <th class="t2">CN_Name：</th>
                <td><input type="text" id="cn_name"/></td>
            </tr>
            <tr>
                <th>EN_Name：</th>
                <td><input type="text" id="en_name"/></td>
            </tr>


            <tr>
                <th>Uri_Path：</th>
                <td><input type="text"  class="longtxt" id="uri_path"/></td>
            </tr>
            <tr>
                <th>In_Level：</th>
                <td>
                    <input type="text" id="in_level"/>
                </td>
            </tr>
            <tr>
                <th>Order_Index：</th>
                <td>
                    <input type="text" id="order_index"/>
                </td>
            </tr>
            <tr>
                <th></th>
                <td>
                    <checkbox>
                        <label><input type="checkbox" id="is_branch" name="is_branch"/><a>Is_Branch</a></label>
                    </checkbox>
                    <checkbox class="mar10-l">
                        <label><input type="checkbox" id="is_disabled" name="is_disabled"/><a style="color: red">Is_Disabled</a></label>
                    </checkbox>
                    <checkbox class="mar10-l">
                        <label><input type="checkbox" id="is_visibled" name="is_visibled"/><a>Is_Visibled</a></label>
                    </checkbox>
                </td>
            </tr>

            <tr>
                <th></th>
                <td>
                    <button onclick="save();return false;">保存</button>
                </td>
            </tr>
        </table>
    </form>
</detail>