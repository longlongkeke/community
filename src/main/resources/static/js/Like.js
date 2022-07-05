function like(btn,entityType,entityId){
 console.log(entityType)
 console.log(entityId)
        $.post(
            "/community/like",
            {
                "entityType":entityType,
                "entityId":entityId
            },
            function(data){
                data=$.parseJSON(data);
                if(data.code==0){
                    $(btn).children("i").text(data.Likecount);
                    $(btn).children("b").text(data.LikeStatus==1?"已赞":"赞");
                }else{
                    alert(data.msg);
                }
            }

        );
}