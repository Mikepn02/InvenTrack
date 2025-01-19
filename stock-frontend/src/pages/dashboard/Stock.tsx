import { Button } from "@mantine/core";
import StockTable from "../../components/tables/StockTable";
import useStock from "../../hooks/useStock";
import { useState } from "react";
import ProductModal from "../../components/modals/product/ProductModal";

const Stock = () => {
  const [isModalOpen , setModalOpen] = useState(false);
  const { stock, topConsumedProducts } = useStock();

  const categories = stock
    ? [...new Set(stock.map((item) => item.category))]
    : [];
  const totalExpenses = stock
    ? stock.reduce((total, item) => total + item.price * item.quantity, 0)
    : 0;
  const lowStockItems = stock ? stock.filter((item) => item.quantity < 10) : [];


  const handleCloseModal = () => {
    setModalOpen(false);
  }

  return (
    <section className="min-h-screen">
      <div className="w-full bg-gray-50 shadow-lg p-4">
        <h1 className="text-2xl font-bold mb-4 text-gray-800">
          Overall Inventory
        </h1>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <div className="bg-blue-100 p-4 rounded-lg shadow-md">
            <h1 className="text-xl font-semibold text-blue-800">Categories</h1>
            <p className="text-3xl font-bold text-blue-900">
              {categories.length}
            </p>
            <p className="text-sm text-gray-600">Last 7 days</p>
          </div>
          <div className="bg-yellow-100 p-4 rounded-lg shadow-md">
            <h1 className="textl-3xl font-semibold text-yellow-800">
              Total Expenses | RWF
            </h1>
            <p className="text-3xl font-bold text-yellow-900">
              {totalExpenses} RWF
            </p>
            <p className="text-sm text-gray-600">Last 7 days</p>
          </div>
          <div className="bg-green-100 p-4 rounded-lg shadow-md">
            <h1 className="textl-3xl font-semibold text-green-800">
              Most consumed
            </h1>
            <p className="text-3xl font-bold text-green-900">
              {topConsumedProducts?.length}
            </p>
            <p className="text-sm text-gray-600">Last 7 days</p>
          </div>

          <div className="bg-red-100 p-4 rounded-lg shadow-md">
            <h1 className="textl-3xl font-semibold text-red-800">Low Stock</h1>
            <p className="text-3xl font-bold text-red-900">
              {lowStockItems.length}
            </p>
            <p className="text-sm text-gray-600">Last 7 days</p>
          </div>
        </div>
      </div>  
      <div className="mt-4 bg-gray-50 shadow-md p-4">
        <div className="flex justify-between">
          <h1 className="text-2xl font-bold mb-6">Items In the Stock</h1>
          <Button className="bg-gray-800 text-white" onClick={() => setModalOpen(true)}>
            + Add product 
          </Button>
        </div>
        <StockTable />
      </div>

      {isModalOpen && (
        <ProductModal 
          onClose={handleCloseModal}
        />
      )}
    </section>
  );
};

export default Stock;
