import { Outlet, useNavigate } from "react-router-dom";//p243 추가
import BasicLayout from "../../layouts/BasicLayout";  //p243 추가
import { useCallback } from "react";//p243 추가

const IndexPage = () => {

    //p243 추가
    const navigate = useNavigate()

    const handleClickList = useCallback(() => {
        navigate({ pathname:'list' })
    })

    const handleClickAdd = useCallback(() => {
        navigate({ pathname:'add' })
  })

  return ( 

    //p243 추가
    <BasicLayout>
      <div className="text-black font-extrabold -mt-10">
          Products Menus
      </div>

      <div className="w-full flex m-2 p-2 ">
        
        <div 
        className="text-xl m-1 p-2  w-20 font-extrabold text-center underline"
        onClick={handleClickList}>
          LIST
        </div>
        
        <div 
        className="text-xl m-1 p-2 w-20 font-extrabold  text-center underline"
        onClick={handleClickAdd}>
          ADD
        </div>
        
      </div>
      <div className="flex flex-wrap w-full ">
        <Outlet/>
      </div>
    </BasicLayout>
   );
}

export default IndexPage;