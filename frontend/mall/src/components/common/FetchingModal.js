const FetchingModal = (  ) => {
    // 파일 업로드시 지연 동작에 대한 처리(모달창을 보여주고 처리가 끝나면 결과를 모달창으로 보여줌)
    return ( 
      <div 
      className={`fixed top-0 left-0 z-[1055] flex h-full w-full  place-items-center justify-center bg-black bg-opacity-20`}>
        <div 
        className=" bg-white rounded-3xl opacity-100 min-w-min h-1/4  min-w-[600px] flex justify-center items-center ">
          
          <div className="text-4xl font-extrabold text-orange-400 m-20">
            Loading.....(처리중입니다.....)
          </div>
        </div>
      </div>  
     );
  }
   
  export default FetchingModal;