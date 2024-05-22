import AddComponent from "../../components/products/AddComponent";  // p248 추가

const AddPage = () => {
//p246 추가
    return (
        <div className="p-4 w-full bg-white">
            <div className="text-3xl font-extrabold">
            Products Add Page  
            </div>
      
        <AddComponent/>  
        {/*  p248 추가 */}
         </div>
    );

}

export default AddPage;