import React from "react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "../../ui/dialog";
import InputField from "../../InputField";
import { Button } from "@mantine/core";
import useStock from "../../../hooks/useStock";
import { getStockStatus } from "../../../lib/utils";

interface ProductModalProps {
  onClose: () => void;
}

const ProductModal = ({ onClose }: ProductModalProps) => {
  const { isLoading , createItem } = useStock();

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const form = e.currentTarget as HTMLFormElement;

    // @ts-ignore
    const name = form.name.value;
    const category = form.category.value;
    const quantity = form.quantity.value;
    const price = form.price.value;

    if (!name || !category || !quantity || !price) {
      console.error("All fields are required.");
      return;
    }
    createItem({
      name,
      category,
      quantity,
      price,
      status: getStockStatus(quantity),
      consumedQuantity: 0,
    });
    form.reset();
    onClose();
  };

  return (
    <Dialog open onOpenChange={onClose}>
      <DialogContent className="max-h-[90vh] overflow-y-scroll no-scrollbar max-w-4xl">
        <DialogHeader className="mb-4 space-y-3">
          <DialogTitle className="font-bold text-xl text-gray-900">
            Create New Stock Item
          </DialogTitle>
        </DialogHeader>

        <form onSubmit={handleSubmit} className="space-y-4">
          <InputField
            label="Product Name"
            name="name"
            placeholder="Enter Product name"
            type="text"
            required
          />
          <InputField
            label="Category"
            name="category"
            placeholder="Enter the Category of product"
            type="text"
            required
          />
          <div className="flex flex-col gap-6 xl:flex-row">
            <InputField
              label="Unit price"
              name="price"
              type="number"
              required
            />

            <InputField
              label="Quantity"
              name="quantity"
              type="number"
              required
            />
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
              loading={isLoading}
              className="bg-gray-800 hover:bg-white hover:border hover:border-gray-900 hover:text-gray-900"
            >
              Create Item
            </Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default ProductModal;
