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

interface RestockItemModalProps {
  onClose: () => void;
  productId: string;
}

const RestockItemModal = ({ onClose, productId }: RestockItemModalProps) => {
  const { isLoading, restock  } = useStock();

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const form = e.currentTarget as HTMLFormElement;
    const additionalQuantity = Number(form.additionalQuantity.value);

    if (!additionalQuantity || additionalQuantity <= 0) {
      console.error("Valid quantity is required.");
      return;
    }

    try {
      await restock(productId, additionalQuantity);
      form.reset();
      onClose();
    } catch (error) {
      console.error("Error consuming stock:", error);
    }
  };

  return (
    <Dialog open onOpenChange={onClose}>
      <DialogContent className="max-h-[90vh] overflow-y-scroll no-scrollbar max-w-4xl">
        <DialogHeader className="mb-4 space-y-3">
          <DialogTitle className="font-bold text-xl text-gray-900">
             Restock Item
          </DialogTitle>
        </DialogHeader>

        <form onSubmit={handleSubmit} className="space-y-4">
          <InputField
            label="Quantity"
            placeholder="Enter the quantity to restock"         
            name="additionalQuantity"
            type="number"
            required
          />
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
            >Restock</Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default RestockItemModal;
