import useSWR, { mutate } from "swr";
import axios from "../lib/axios.config";

interface ReportParams {
  startDate: string;
  endDate: string;
  supplierId?: number;
}

export default function useReport() {
  const {
    data: reports,
    isLoading,
    error,
  } = useSWR<Blob | null>(
    null,
    async (params: ReportParams) => {
      const { startDate, endDate, supplierId } = params;
      const endpoint = supplierId
        ? "/report/supplier-performance"
        : "/report/order-history";

      const response = await axios.get(endpoint, {
        params: supplierId
          ? { supplierId, startDate, endDate }
          : { startDateStr: startDate, endDateStr: endDate },
        responseType: "blob",
      });
      return response.data;
    },
    { revalidateOnFocus: false }
  );


  const fetchStockReport = async () => {
    try {
      const response = await axios.get("/report/stock-levels", {
        responseType: "blob",
      });
      const fileUrl = URL.createObjectURL(response.data);
      const link = document.createElement("a");
      link.href = fileUrl;
      link.setAttribute("download", "stock_level_report.pdf");
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      mutate(response.data, false);
    } catch (error) {
      console.error("Failed to fetch the stock level report: ", error);
    }
  };

  const fetchReport = async (params: ReportParams) => {
    try {
      const response = await axios.get("/report/order-history", {
        params: { startDateStr: params.startDate, endDateStr: params.endDate },
        responseType: "blob",
      });
      const fileUrl = URL.createObjectURL(response.data);
      const link = document.createElement("a");
      link.href = fileUrl;
      link.setAttribute(
        "download",
        `order_history_report_${params.startDate}_${params.endDate}.pdf`
      );
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);

      mutate(response.data, false);
    } catch (error) {
      console.error("Failed to fetch the report: ", error);
    }
  };

  const fetchReportExcel = async (params: ReportParams) => {
    try {
      const response = await axios.get("/report/order-history-excel", {
        params: { startDateStr: params.startDate, endDateStr: params.endDate },
        responseType: "blob",
      });
      const fileUrl = URL.createObjectURL(response.data);
      const link = document.createElement("a");
      link.href = fileUrl;
      link.setAttribute(
        "download",
        `order_history_excel_${params.startDate}_${params.endDate}.xlsx`
      );
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);

      mutate(response.data, false);
    } catch (error) {
      console.error("Failed to fetch the report: ", error);
    }
  };

  const fetchOrderHistoryPDF = async (params: ReportParams) => {
    try {
      const response = await axios.get("/report/order-history", {
        params: { startDateStr: params.startDate, endDateStr: params.endDate },
        responseType: "blob",
      });
      const fileUrl = URL.createObjectURL(response.data);
      const link = document.createElement("a");
      link.href = fileUrl;
      link.setAttribute(
        "download",
        `order_history_report_${params.startDate}_${params.endDate}.pdf`
      );
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);

      mutate(response.data, false);
    } catch (error) {
      console.error("Failed to fetch the PDF report: ", error);
    }
  };

  const fetchSupplierPerformanceReport = async (params: ReportParams) => {
    try {
      const response = await axios.get("/report/supplier-performance", {
        params: {
          supplierId: params.supplierId,
          startDate: params.startDate,
          endDate: params.endDate,
        },
        responseType: "blob",
      });
      const fileUrl = URL.createObjectURL(response.data);
      const link = document.createElement("a");
      link.href = fileUrl;
      link.setAttribute(
        "download",
        `supplier_performance_report_${params.startDate}_${params.endDate}.pdf`
      );
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);

      mutate(response.data, false);
    } catch (error) {
      console.error("Failed to fetch the supplier performance report: ", error);
    }
  };

  const fetchAllSuppliersPerfomance = async (params: ReportParams) => {
    try {
      const response = await axios.get("/report/all-supplier-performance", {
        params: {
          startDate: params.startDate,
          endDate: params.endDate,
        },
        responseType: "blob",
      });
      const fileUrl = URL.createObjectURL(response.data);
      const link = document.createElement("a");
      link.href = fileUrl;
      link.setAttribute(
        "download",
        `all_supplier_performance_report_${params.startDate}_${params.endDate}.pdf`
      );
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);

      mutate(response.data, false);
    } catch (error) {
      console.error("Failed to fetch the supplier performance report: ", error);
    }
  };

  const fetchOrderHistoryExcel = async (params: ReportParams) => {
    try {
      const response = await axios.get("/report/order-history-excel", {
        params: { startDateStr: params.startDate, endDateStr: params.endDate },
        responseType: "blob",
      });
      const fileUrl = URL.createObjectURL(response.data);
      const link = document.createElement("a");
      link.href = fileUrl;
      link.setAttribute(
        "download",
        `order_history_excel_${params.startDate}_${params.endDate}.xlsx`
      );
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);

      mutate(response.data, false);
    } catch (error) {
      console.error("Failed to fetch the Excel report: ", error);
    }
  };

  return {
    reports,
    fetchReport,
    fetchReportExcel,
    fetchOrderHistoryExcel,
    fetchOrderHistoryPDF,
    fetchSupplierPerformanceReport,
    fetchAllSuppliersPerfomance,
    fetchStockReport,
    isLoading,
    error,
  };
}
