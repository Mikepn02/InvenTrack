import React, { useState } from "react";
import SupplierTable from "../../components/tables/SupplierTable";
import { Button } from "@mantine/core";
import SupplierModal from "../../components/modals/supplier/SupplierModal";

const Supplier = () => {
  const [isModalOpen, setModalOpen] = useState(false);

  const handleCloseModal = () => {
    setModalOpen(false);
  };
  return (
    <div>
      <div className="flex flex-col md:flex-row justify-between items-center my-5">
        <h1 className="text-xl font-bold text-gray-900 my-5">
          Here are all suppliers:{" "}
        </h1>
        <Button
          color="#111827"
          className="w-full md:w-auto"
          onClick={() => setModalOpen(true)}
        >
          + New Supplier
        </Button>
      </div>
      <div>
        <SupplierTable />
      </div>

      {isModalOpen && <SupplierModal onClose={handleCloseModal} />}
    </div>
  );
};

export default Supplier;
