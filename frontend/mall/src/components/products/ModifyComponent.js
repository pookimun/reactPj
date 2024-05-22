import { useEffect, useRef, useState } from "react"; // p284 추가
import { getOne, putOne, deleteOne } from "../../api/productsApi"; // p284 추가
import FetchingModal from "../common/FetchingModal";  // p284 추가
import { API_SERVER_HOST } from "../../api/todoApi"; // p285 추가
import useCustomMove from "../../hooks/useCustomMove";
// 514 제거 import ResultModal from "../common/ResultModal";

// useQuery()를 이용해서 상품 데이터 가져오는 코드 작성
// 1. useQuery()를 이용해서 상품 데이터를 가져온 후 컴포넌트의 상태 값으로 지정
// 2. <input>을 이용해서 컴포넌트의 상태로 유지되는 데이터를 수정
// 3. 수정이나 삭제를 처리한 후 화면을 이동
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query"; // 512추가 518 추가 useMutation, , useQueryClient 
import ResultModal from "../common/ResultModal"; // 518 결과 모달창 추가

const initState = {
  pno:0,
  pname: '',
  pdesc: '',
  price: 0,
  delFlag:false,
  uploadFileNames:[]
}

const host = API_SERVER_HOST  // p285 추가

// p282 추가
const ModifyComponent = ({pno}) => {

  const [product, setProduct] = useState(initState) // p284 추가

  // 515 제거 //결과 모달
  // 515 제거 const [result, setResult] = useState(null)
  //이동용 함수
  const {moveToRead, moveToList} = useCustomMove()

  // 515 제거 const [fetching, setFetching] = useState(false) // p284 추가


  const uploadRef = useRef() // p285 추가

  //513 추가 
  const query = useQuery(
    ['products', pno],
    () => getOne(pno),
    //  513 추가시 if(query.isSuccess) {
    //    setProduct(query.data)
    // } 무한 반복코드가 진행됨
    { // 반복 코드 해결용 useEffect()를 이용해 온전히 데이터가 존재하고 성공 했을 경우 setProduct() 호출
      staleTime: Infinity // (무한)
    }
  )

  useEffect(() => { // p284 추가
    // 514 추가
    if(query.isSuccess){
      setProduct(query.data)
    }

    // 514 제거 setFetching(true)

    // getOne(pno).then(data => {

    //   setProduct(data)
    //   setFetching(false)
    // } )

  },[pno, query.data, query.isSuccess]) // p284 추가 514 추가 , query.data, query.isSuccess

  // 518 추가
  const delMutation = useMutation((pno) => deleteOne(pno))
  // 518 추가 
  const queryClient = useQueryClient()
  
  //p286 추가
  const handleChangeProduct = (e) => {

    product[e.target.name] = e.target.value

    setProduct({...product})
  }
  //p286 추가
  const deleteOldImages = (imageName) => {

    const resultFileNames = product.uploadFileNames.filter( fileName => fileName !== imageName)

    product.uploadFileNames = resultFileNames

    setProduct({...product}) //p286 추가
  }
  // 519 추가
  const modMutation = useMutation((product) => putOne(pno, product))

  const handleClickModify = () => {

    const files = uploadRef.current.files

    const formData = new FormData()

    for (let i = 0; i < files.length; i++) {
      formData.append("files", files[i]);
    }
    
    //other data
    formData.append("pname", product.pname)
    formData.append("pdesc", product.pdesc)
    formData.append("price", product.price)
    formData.append("delFlag", product.delFlag)

    for( let i = 0; i < product.uploadFileNames.length ; i++){
      formData.append("uploadFileNames", product.uploadFileNames[i])
    }    

    modMutation.mutate(formData) // 519 추가
    //fetching
    // 516 제거 setFetching(true)

    // 516 제거 putOne(pno, formData).then(data => { //수정 처리
    // 516 제거   setResult('Modified')
    // 516 제거   setFetching(false)
    // 516 제거 })

  
  }

  const handleClickDelete = () => {

    delMutation.mutate(pno) // 518 추가 
    //  516 제거  setFetching(true)
    //  516 제거 deleteOne(pno).then(data => {

    //   516 제거  setResult("Deleted")
    //   516 제거  setFetching(false)

    //  516 제거  })

  }

  const closeModal = () => {
    
    // 518 추가
    if(delMutation.isSuccess) {
      queryClient.invalidateQueries(['products', pno])
      queryClient.invalidateQueries(['products/list'])
      moveToList()
      return
    }

    // 520 추가
    if(modMutation.isSuccess) {
      queryClient.invalidateQueries(['products', pno])
      queryClient.invalidateQueries(['products/list'])
      moveToRead(pno)
    }
    //  516 제거 if(result ==='Modified') {
    //  516 제거   moveToRead(pno)
    //  516 제거 }else if(result === 'Deleted') {
    //  516 제거   moveToList({page:1})
    //  516 제거 }

    //  516 제거 setResult(null)

  }

  return ( 
  <div className = "border-2 border-sky-200 mt-10 m-2 p-4"> 
     {/* 516 제거 p284 추가
    {fetching? <FetchingModal/> :<></>}

    {result? 
      <ResultModal
      title={`${result}`}
      content={'정상적으로 처리되었습니다.'}  //결과 모달창 
      callbackFn={closeModal}
      />    
    :
    <></>
    } */}
    {/* 516 FetchingModal 추가 */}
    {/* 518 || delMutation.isLoading 추가  */}
    {/* 520 || modMutation.isLoading 추가 */}
    {query.isFetching  || delMutation.isLoading || modMutation.isLoading ?
    <FetchingModal/>
    :
    <></>
    }
  {/* 518 추가  */}
  {/*  || modMutation.isSuccess */}
  {/* 521추가 || modMutation.isSuccess */}
  {
      delMutation.isSuccess || modMutation.isSuccess ?
      <ResultModal
      title={'처리 결과'}
      content={'정상적으로 처리되었습니다.'}
      callbackFn={closeModal}>

      </ResultModal>
      :
      <></>
    }
    <div className="flex justify-center">
      <div className="relative mb-4 flex w-full flex-wrap items-stretch">
        <div className="w-1/5 p-6 text-right font-bold">Product Name</div>
        <input className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md" 
          name="pname"
          type={'text'} 
          value={product.pname}
          onChange={handleChangeProduct}
          >
          </input>

      </div>
    </div>
    <div className="flex justify-center">
      <div className="relative mb-4 flex w-full flex-wrap items-stretch">
        <div className="w-1/5 p-6 text-right font-bold">Desc</div>
          <textarea 
          className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md resize-y"
          name="pdesc"
          rows="4"
          onChange={handleChangeProduct}
          value={product.pdesc}>
            {product.pdesc}
          </textarea>
        </div>  
    </div>
    <div className="flex justify-center">
      <div className="relative mb-4 flex w-full flex-wrap items-stretch">
        <div className="w-1/5 p-6 text-right font-bold">Price</div>
        <input className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md" 
          name="price"
          type={'number'} 
          value={product.price}
          onChange={handleChangeProduct}
          >
          </input>
      </div>
    </div>

    <div className="flex justify-center">
      <div className="relative mb-4 flex w-full flex-wrap items-stretch">
        <div className="w-1/5 p-6 text-right font-bold">DELETE</div>
          <select 
          name="delFlag" value={product.delFlag}
          onChange={handleChangeProduct}
          className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md">
            <option value={false}>사용</option>
            <option value={true}>삭제</option>
          </select>
      </div>
    </div>

    <div className="flex justify-center">
      <div className="relative mb-4 flex w-full flex-wrap items-stretch">
        <div className="w-1/5 p-6 text-right font-bold">Files</div>
        <input ref={uploadRef} 
          className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md" 
          type={'file'} multiple={true}
        >    
        </input>
      </div>
    </div>
    <div className="flex justify-center">
      <div className="relative mb-4 flex w-full flex-wrap items-stretch">
        <div className="w-1/5 p-6 text-right font-bold">
          Images          
        </div>
        <div className="w-4/5 justify-center flex flex-wrap items-start">
        
        {product.uploadFileNames.map( (imgFile, i) => 
            <div
              className="flex justify-center flex-col w-1/3" 
              key = {i}>
              <button className="bg-blue-500 text-3xl text-white"
              onClick={() => deleteOldImages(imgFile)}
              >DELETE</button>
              <img 
              alt ="img" 
              src={`${host}/api/products/view/s_${imgFile}`}/>
              
            </div>
        )}


        </div>
      </div>
    </div>

    <div className="flex justify-end p-4">
      <button type="button" 
      className="rounded p-4 m-2 text-xl w-32 text-white bg-red-500"
      onClick={handleClickDelete}
      >
        Delete
      </button>

      <button type="button" 
      className="inline-block rounded p-4 m-2 text-xl w-32  text-white bg-orange-500"
      onClick={handleClickModify}
      >
        Modify
      </button>
      
      <button type="button" 
      className="rounded p-4 m-2 text-xl w-32 text-white bg-blue-500"
      onClick={moveToList}
      >
        List
      </button>

    </div>

  </div>
  );
}
 
export default ModifyComponent;