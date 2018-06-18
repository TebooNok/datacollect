
        /**
         * ajax封装
         * url 发送请求的地址
         * data 发送到服务器的数据，数组存储，如：{"username": "张三", "password": 123456}
         * succCallback 成功回调函数
         * errorCallback 失败回调函数
         * type 请求方式("POST" 或 "GET")， 默认已经设置为 "POST"
         * dataType 预期服务器返回的数据类型，常用的如：xml、html、json、text
         * reference jquery-1.7.1.js
         */

        //插入loading

        function $ajax(url, postData, succCallback, errorCallback, type, dataType){
            var type = type || "post";
            var dataType = dataType || "json";
            var token = window.localStorage.getItem('token') ? window.localStorage.getItem('token') : '';
            var isLogin = postData.isLogin;
            console.log(1234)
            if (!token.length && !isLogin) {
          
                window.location.href = '/login.html';
                return false
              }
            $.ajax({
                type: type,
                url: 'http://19.0.1.20:8081/' + url,
                data: postData,
                dataType: dataType,
                headers: !isLogin ? {'Content-Type':'application/x-www-form-urlencoded',"Authorization": token} : {'Content-Type':'application/x-www-form-urlencoded'},

                success: function(res){
                    succCallback(res)
                },
                error: function(res) {
                    errorCallback(res)
                }
            });
        }