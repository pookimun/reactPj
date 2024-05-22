import { useParams } from "react-router-dom";
import ReadComponent from "../../components/todo/ReadComponent"; // p134 추가
// p144 제거 import { useCallback } from "react";
// p144 제거 import { createSearchParams, useNavigate, useParams, useSearchParams } from "react-router-dom";  //p74추가 
// p75 추가 createSearchParams, useSearchParams



const ReadPage = () => {

    const {tno} = useParams()

    
//     p144 제거 const navigate = useNavigate()

//     // todo/modify/xx로 이동시 필요한 쿼리스트링을 만들어내서 naviagte()를 이용한 이동시 활용
//     const [queryParams] = useSearchParams() 
//     const page = queryParams.get("page") ? parseInt(queryParams.get("page")) : 1
//     const size = queryParams.get("size") ? parseInt(queryParams.get("size")) : 10
//     const queryStr = createSearchParams({page,size}).toString()
     
//     const moveToModify = useCallback((tno) => {

//         navigate({
//           pathname: `/todo/modify/${tno}`, 
//           search: queryStr
//         })
        
//     },[tno, page, size])    //p76쿼리스트링 page, size 추가
//     const moveToList = useCallback(() => {

//         navigate({
//             pathname:`/todo/list`, 
//             search: queryStr
//         })
//     }, [page, size])
// // p77 조회에서 목록으로 이동 함수 추가

    return (
        <div className="font-extrabold w-full bg-white mt-6">
            <div className="text-2xl">
            Todo Read Page Component {tno}
            </div> 
            <ReadComponent tno={tno}></ReadComponent> 
            {/* p134 추가 */}
             
            {/* p74 추가 -> p134제거
            <div>
                <button onClick={() => moveToModify(tno)}>Test Modify</button>
                 // p76 수정으로 가는 버튼으로 변경 

                 <button onClick={() => moveToList()}>Test List</button>
                 // p77 리스트로 가는 버튼 추가 
            </div>  */}

        </div>
    );    

}

export default ReadPage;