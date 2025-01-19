import { Button, Select } from "@mantine/core";
import React, { useState } from "react";
import SearchInput from "../../components/SearchInput";
import { DataTable } from "../../components/tables/DataTable";
import OrderTable from "../../components/tables/OrderTable";
import OrderModal from "../../components/modals/order/OrderModal";
import ExportModal from "../../components/modals/order/ExportModal";

const Order = () => {
  const [isModalOpen , setModalOpen] = useState(false);
  const [isExcelModalOpen , setExcelModalOpen] = useState(false);
  const handleSearch = () => {};

  const handleCloseModal = () => {
    setModalOpen(false);
    setExcelModalOpen(false);
 };

  return (
    <section className="w-full">
      <div className="flex flex-col md:flex-row justify-between items-start md:items-center">
        <h1 className="text-xl md:text-2xl font-bold text-gray-800">Orders</h1>

        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mt-4 md:mt-0 w-full md:w-auto">
          <Button
            className="bg-white text-gray-800 border-2 border-gray-800"
            fullWidth
            onClick={() => setExcelModalOpen(true)}
          >
            Export to Excel
          </Button>
          <Button
            className="bg-white text-gray-800 border-2 border-gray-800"
            fullWidth
          >
            Import Orders
          </Button>
          <Button className="bg-gray-800 text-white" fullWidth onClick={() => setModalOpen(true)}>
            + New Orders
          </Button>
        </div>
      </div>
      <div className="mt-4">
        <OrderTable />
        {isModalOpen && (
          <OrderModal 
            onClose={handleCloseModal}
          />
        )}

        {isExcelModalOpen && (
          <ExportModal  
            onClose={handleCloseModal}
          />
        )}
      </div>
    </section>
  );
};

export default Order;
