/*
 * @Description: 
 * @version: 
 * @Author: Hinsane
 * @Date: 2022-05-01 18:32:58
 * @LastEditors: Hinsane
 * @LastEditTime: 2022-05-18 11:48:58
 */

function RouterListFormat(data){
    let parents = data.filter(p => p.pid===0),
        children = data.filter(c => c.pid != 0)
    
        dataTotree(parents,children)
        //递归函数
        function dataTotree(parents,children){
            parents.map((p)=>{
                children.map((c,index)=>{
                    if(c.pid === p.rid){//如果相同，深拷贝对象c
                        let c1 = Object.assign([],children)
                        c1.splice(index,1)
                        dataTotree([c],c1);
                        if(p.children){
                            p.children.push(c)
                        }else{
                            p.children = [c]
                        }
                    }
                })
            })
        }
    return parents
}


//转化为路由表格式
function generateRouter(UserRouter){
    let newRouters = UserRouter.map((r)=>{
        let routes = {
            path:r.path,
            name:r.name,
            component:()=>import(`@/views/${r.name}.vue`)
        }
        if(r.children){
            routes.children = generateRouter(r.children)
        }
        return routes
    })
    return newRouters
}
export{
    RouterListFormat,
    generateRouter
}



