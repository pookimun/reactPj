import ListComponent from "../../components/todo/ListComponent";
import { useSearchParams } from "react-router-dom"; // p69추가 (쿼리스트링 처리)

const ListPage = () => {

    const [ queryParams ] = useSearchParams()
    // const page = queryParams.get("page") ? parseInt(queryParams.get("page")) : 1
    // const size = queryParams.get("size") ? parseInt(queryParams.get("size")) : 10


  return ( 
  <div className="p-4 w-full bg-white">
    <div className="text-3xl font-extrabold">
      Todo List Page Component (리스트페이지)
      <ListComponent/>
    </div> 

    

  </div>
   );
}
 
export default ListPage;



// const ListPage = () => {

//     

//     const page = queryParams.get("page") ? parseInt(queryParams.get("page")) : 1
//    

//     return (
//         <div className="p-4 w-full bg-white">
//             <div className="text-3xl font-extrabold">
//                 Todo List Page Component {page} ---------{size}        
//             </div>
//         </div>
//     );
// }

// export default ListPage;