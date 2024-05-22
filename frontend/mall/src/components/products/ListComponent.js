// 501 제거 import { useEffect, useState } from "react";
import { getList } from "../../api/productsApi";
import useCustomMove from "../../hooks/useCustomMove";
import FetchingModal from "../common/FetchingModal";

import { API_SERVER_HOST } from "../../api/todoApi"; // p267추가
import PageComponent from "../common/PageComponent"; // p269추가 (상품 클릭시 페이지 이동)
import useCustomLogin from "../../hooks/useCustomLogin"; // p397 추가 (로그인 연동)
import { useQuery, useQueryClient } from "@tanstack/react-query"; // 501 추가 //504추가, useQueryClient

// 501 제거 const host = API_SERVER_HOST // p267추가

const initState = {
    dtoList:[],
    pageNumList:[],
    pageRequestDTO: null,
    prev: false,
    next: false,
    totoalCount: 0,
    prevPage: 0,
    nextPage: 0,
    totalPage: 0,
    current: 0
  }

  const host = API_SERVER_HOST // 501 추가

  const ListComponent = () => {

    const {moveToLoginReturn} = useCustomLogin() // 501 추가 

    // 501 제거 const {exceptionHandle} = useCustomLogin() // p398 추가

    const {page, size, refresh, moveToList, moveToRead} = useCustomMove()
    
    // 501 추가 // 506 refresh 기능 추가(약간의 딜레이 적용으로 서버 호출 막음)
    const {isFetching, data, error, isError} = useQuery( 
      ['products/list' , {page,size, refresh}],    //, refresh
      () => getList({page,size}), 
      {staleTime: 1000 * 5 }  // 506 추가
    )
    // 504 추가
    const queryClient = useQueryClient()

    // 504 추가  
    const handleClickPage = (pageParam) => {
    if(pageParam.page === parseInt(page)){
      queryClient.invalidateQueries("products/list")
    }
    moveToList(pageParam)
    }

    // 501 추가
    if(isError) {
      console.log(error)
      return moveToLoginReturn()
    }

    //serverData는 나중에 사용
    // 501 변경 const [serverData, setServerData] = useState(initState)
    const serverData = data || initState
    //for FetchingModal 
    // 501 제거 const [fetching, setFetching] = useState(false)
  
    // 501 제거  useEffect(() => {
  
    //   setFetching(true)
  
    //   getList({page,size}).then(data => {
    //     console.log(data)
    //     setServerData(data)
    //     setFetching(false)
    //   }).catch( err => exceptionHandle(err)) // p398추가
  
    // }, [page,size, refresh])
  
    return ( 
        <div className="border-2 border-blue-100 mt-10 mr-2 ml-2">
            {/* 501추가 */}
            {isFetching? <FetchingModal/> :<></>}
            {/* 501 제거 <h1>Products List Component</h1>
        
        {fetching? <FetchingModal/> :<></>} */}
        <div className="flex flex-wrap mx-auto p-6">
  
        {serverData.dtoList.map(product =>
  
        <div
        key= {product.pno} 
        className="w-1/2 p-1 rounded shadow-md border-2"
        onClick={() => moveToRead(product.pno)}
        >  
  
          <div className="flex flex-col  h-full">
            <div className="font-extrabold text-2xl p-2 w-full ">
              {product.pno}
            </div>
            <div className="text-1xl m-1 p-2 w-full flex flex-col">
              
              <div className="w-full overflow-hidden ">
                <img alt="product"
                className="m-auto rounded-md w-60" 
                src={`${host}/api/products/view/s_${product.uploadFileNames[0]}`}/>
              </div>
  
              <div className="bottom-0 font-extrabold bg-white">
                <div className="text-center p-1">
                  이름: {product.pname}
                </div>
                <div className="text-center p-1">
                  가격: {product.price}
                </div>
              </div>
  
            </div>
          </div>
        </div>
        )}
        </div>
          {/*  p270 페이징 기능 추가 */}
        {/*  504 변경 <PageComponent serverData={serverData} movePage={moveToList}></PageComponent>  */}
        <PageComponent serverData={serverData} movePage={handleClickPage}></PageComponent>
      </div>
    );
}

export default ListComponent;
