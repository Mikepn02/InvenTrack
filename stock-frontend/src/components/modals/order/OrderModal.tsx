import React from "react";
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "../../ui/dialog";
import InputField from "../../InputField";
import { Button, Select } from "@mantine/core";
import useSupplier from "../../../hooks/useSupplier";
import useStock from "../../../hooks/useStock";
import useOrder from "../../../hooks/useOrder";

interface OrderModalProps {
  onClose: () => void;
}

const OrderModal = ({ onClose }: OrderModalProps) => {
  const { suppliers, isLoading: suppliersLoading } = useSupplier();
  const { stock: stockItems } = useStock();
  const { createOrder } = useOrder();

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const form = e.currentTarget as HTMLFormElement;
    // @ts-ignore
    const name = form.name.value;
    const supplierName = form.supplierName.value;
    const status = form.status.value;
    const date = form.date.value;
    const stockItemName = form.stockItemName.value;
    const quantity = form.quantity.value;

    if (!name || !supplierName || !status || !date || !stockItemName || !quantity) {
      console.error("All fields are required.");
      return;
    }
    createOrder({
      name,
      supplierName,
      status,
      date,
      stockItemName,
      quantity
    });
    form.reset();
    onClose();
  };

  return (
    <Dialog open onOpenChange={onClose}>
      <DialogContent className="max-h-[90vh] overflow-y-scroll no-scrollbar max-w-4xl">
        <DialogHeader className="mb-4 space-y-3">
          <DialogTitle className="text-xl md:text-3xl font-bold">Create Order</DialogTitle>
        </DialogHeader>

        <form onSubmit={handleSubmit} className="space-y-4">
          <InputField
            label="Order Name"
            name="name"
            placeholder="Enter order name"
            type="text"
            required
          />

          <div>
            <label className="block text-sm font-medium text-gray-700">Supplier Name</label>
            <select
              name="supplierName"
              className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
              required
            >
              <option value="">{suppliersLoading ? "Loading suppliers..." : "Select a supplier"}</option>
              {suppliers?.map((supplier) => (
                <option key={supplier.companyName} value={supplier.companyName}>
                  {supplier.companyName}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">Status</label>
            <select
              name="status"
              className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
              required
            >
              <option value="">Select a status</option>
              <option value="PENDING">Pending</option>
              <option value="COMPLETED">Completed</option>
              <option value="FAILED">Failed</option>
            </select>
          </div>

          <InputField
            label="Order Date"
            name="date"
            type="date"
            required
          />
          <InputField
            label="Quantity"
            name="quantity"
            placeholder="Enter The quantity"
            type="number"
            required
          />

          <div>
            <label className="block text-sm font-medium text-gray-700">Stock Item Name</label>
            <select
              name="stockItemName"
              className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
              required
            >
              <option value="">Select a stock item</option>
              {stockItems?.map((item) => (
                <option key={item.name} value={item.name}>
                  {item.name}
                </option>
              ))}
            </select>
          </div>

          <div className="flex justify-end space-x-2">
            <Button
              type="button"
              variant="outline"
              onClick={onClose}
              className="hover:bg-gray-800 border border-gray-900 text-gray-900 hover:text-white"
            >
              Cancel
            </Button>
            <Button
              type="submit"
              variant="solid"
              className="bg-gray-800 hover:bg-white hover:border hover:border-gray-900 hover:text-gray-900"
            >
              Create Order
            </Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default OrderModal;
