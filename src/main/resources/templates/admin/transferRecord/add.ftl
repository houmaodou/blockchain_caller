<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>转移记录添加--${site.name}</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="format-detection" content="telephone=no">
    <meta name="description" content="${site.description}"/>
    <meta name="keywords" content="${site.keywords}"/>
    <meta name="author" content="${site.author}"/>
    <link rel="icon" href="${site.logo}">
    <link rel="stylesheet" href="${base}/static/layui/css/layui.css" media="all" />
    <style type="text/css">
        .layui-form-item .layui-inline{ width:33.333%; float:left; margin-right:0; }
        @media(max-width:1240px){
            .layui-form-item .layui-inline{ width:100%; float:none; }
        }
        .layui-form-item .role-box {
            position: relative;
        }
        .layui-form-item .role-box .jq-role-inline {
            height: 100%;
            overflow: auto;
        }

    </style>
</head>
<body class="childrenBody">
<form id="tf" class="layui-form" style="width:80%;" enctype="multipart/form-data">
    <div class="layui-form-item">
        <label class="layui-form-label">机构代码</label>
        <div class="layui-input-block">

            <select name="transferOrgCode" lay-verify="required">
                <option value="" selected="">请选择机构代码</option>
                <@my type="transfer_record_transfer_org_code">
                <#list result as r>
                <option value="${r.value}" >${r.label}</option>
                </#list>
                </@my>
            </select>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">转移方向</label>
        <div class="layui-input-block">

            <select name="direction" lay-verify="required">
                <option value="" selected="">请选择转移方向</option>
                <@my type="transfer_record_direction">
                <#list result as r>
                <option value="${r.value}" >${r.label}</option>
                </#list>
                </@my>
            </select>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">Excel 文件</label>
        <div class="layui-input-block">

            <input type="file" class="layui-input" name="file" id="file" >

        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit="" lay-filter="addTransferRecord">立即提交</button>
            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
        </div>
    </div>
</form>
<script type="text/javascript" src="${base}/static/layui/layui.js"></script>
<script>
    layui.use(['form','jquery','layer','upload'],function(){
        var form      = layui.form,
                $     = layui.jquery,
                upload = layui.upload,
                layer = layui.layer;


        form.on("submit(addTransferRecord)",function(data){
                       if(null === data.field.file || "" ===data.field.file){
                        layer.msg("文件必须上传");
                        return false;
                    }

            var loadIndex = layer.load(2, {
                shade: [0.3, '#333']
            });
            <#--$.post("${base}/admin/transferRecord/add",data.field,function(res){-->
                <#--layer.close(loadIndex);-->
                <#--if(res.success){-->
                    <#--parent.layer.msg("转移记录添加成功！",{time:1000},function(){-->
                        <#--parent.layer.close(parent.addIndex);-->
                        <#--//刷新父页面-->
                        <#--parent.location.reload();-->
                    <#--});-->
                <#--}else{-->
                    <#--layer.msg(res.message);-->
                <#--}-->
            <#--});-->
            var form = new FormData(document.getElementById("tf"));
            $.ajax({
                url:"${base}/admin/transferRecord/add",
                type:"post",
                data:form,
                processData:false,
                contentType:false,
                success:function(res){
                    layer.close(loadIndex);
                    if(res.success){
                        parent.layer.msg("转移记录添加成功！",{time:1000},function(){
                            parent.layer.close(parent.addIndex);
                            //刷新父页面
                            parent.location.reload();
                        });
                    }else {
                        layer.msg(res.message);
                    }

                },
                error:function(res){
                    layer.close(loadIndex);
                    layer.msg(res.message);
                }
            });
            return false;
        });

    });
</script>
</body>
</html>