import React from "react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "../../ui/dialog";
import InputField from "../../InputField";
import { Button } from "@mantine/core";
import useReport from "../../../hooks/useReport";

interface ExportModalProps {
  onClose: () => void;
}
const ExportModal = ({ onClose }: ExportModalProps) => {

    const { fetchReportExcel , fetchReport} = useReport()

  const handleSubmit = async(e: React.FormEvent<HTMLFormElement> ) => {
    const form = e.currentTarget as HTMLFormElement;
    const startDate = form.startDate.value;
    const endDate = form.endDate.value;
    fetchReportExcel({startDate , endDate});

    form.reset();
    onClose();
  }
  return (
    <Dialog open onOpenChange={onClose}>
      <DialogContent className="max-h-[90vh] overflow-y-scroll no-scrollbar max-w-4xl">
        <DialogHeader className="mb-4 space-y-3">
          <DialogTitle>Export to Excel</DialogTitle>
        </DialogHeader>

        <form onSubmit={handleSubmit} className="space-y-4">
          <InputField label="Start Date" name="startDate" type="date" required />
          <InputField label="End Date" name="endDate" type="date" required />

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
              Generate Report
            </Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default ExportModal;
