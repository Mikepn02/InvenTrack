import React from "react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "../../ui/dialog";
import InputField from "../../InputField";
import { Button } from "@mantine/core";
import useSupplier from "../../../hooks/useSupplier";

interface SupplierModalProps {
  onClose: () => void;
}

const SupplierModal = ({ onClose }: SupplierModalProps) => {
  const { isLoading, createSupplier } = useSupplier();

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const form = e.currentTarget as HTMLFormElement;

    // @ts-ignore
    const companyName = form.companyName.value;
    const companyEmail = form.companyEmail.value;
    const companyPhoneNumber = form.companyPhoneNumber.value;
    const companyHolder = form.companyHolder.value;
    const taxId = form.taxId.value;

    if (!companyName || !companyEmail || !companyPhoneNumber || !taxId) {
      console.error("All fields are required.");
      return;
    }

    createSupplier({
      companyName,
      companyEmail,
      companyHolder,
      taxId,
      companyPhoneNumber,
    });

    form.reset();
    onClose();
  };

  return (
    <Dialog open onOpenChange={onClose}>
      <DialogContent className="max-h-[90vh] overflow-y-scroll no-scrollbar max-w-4xl">
        <DialogHeader className="mb-4 space-y-3">
          <DialogTitle className="font-bold text-xl text-gray-900">
            Create New Supplier
          </DialogTitle>
        </DialogHeader>

        <form onSubmit={handleSubmit} className="space-y-4">
          <InputField
            label="Company Name"
            name="companyName"
            placeholder="Enter Company name"
            type="text"
            required
          />
          <InputField
            label="Company Holder"
            name="companyHolder"
            placeholder="Enter the Company Holder name"
            type="text"
            required
          />
          <InputField
            label="Company Email"
            name="companyEmail"
        placeholder="Enter Company Email"
            type="text"
            required
          />
          <div className="flex flex-col gap-6 xl:flex-row">
            <InputField
              label="Company Phone Number"
              name="companyPhoneNumber"
              type="text"
              required
            />

            <InputField
              label="TIN"
              name="taxId"
              type="text"
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
              Create Supplier
            </Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default SupplierModal;
