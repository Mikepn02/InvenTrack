import { ColumnDef } from "@tanstack/react-table";
import { useState } from "react";
import { Button, Select } from "@mantine/core";
import { Order } from "../../types";
import { Checkbox } from "../ui/checkbox";
import useOrder from "../../hooks/useOrder";
import { DataTable } from "./DataTable";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "../ui/dropdown-menu";
import ConfirmationModal from "../modals/order/ConfirmationModal";
import { MoreVerticalIcon } from "lucide-react";
import SearchInput from "../SearchInput";

const columns: ColumnDef<Order>[] = [
  {
    id: "select",
    header: ({ table }) => (
      <Checkbox
        checked={
          table.getIsAllPageRowsSelected() ||
          (table.getIsSomePageRowsSelected() && "indeterminate")
        }
        onCheckedChange={(value) => table.toggleAllPageRowsSelected(!!value)}
        aria-label="Select all"
      />
    ),
    cell: ({ row }) => (
      <Checkbox
        checked={row.getIsSelected()}
        onCheckedChange={(value) => row.toggleSelected(!!value)}
        aria-label="Select row"
      />
    ),
    enableSorting: false,
    enableHiding: false,
  },
  {
    id: "index",
    header: "#",
    cell: ({ row }) => row.index + 1,
  },
  {
    accessorKey: "name",
    header: "Name",
  },
  {
    accessorKey: "supplierName",
    header: "Supplier",
  },
  {
    accessorKey: "quantity",
    header: "Quantity",
  },
  {
    accessorKey: "stockItemName",
    header: "Item",
  },
  {
    accessorKey: "date",
    header: "Date",
  },
  {
    accessorKey: "status",
    header: "Status",
    cell: ({ row }) => {
      const status = row.original.status;
      let statusClass = "";
      if (status === "PENDING") {
        statusClass = "bg-yellow-100  text-yellow-800";
      } else if (status === "DELIVERED") {
        statusClass = "bg-green-100 text-green-800";
      } else if (status === "FAILED") {
        statusClass = "bg-red-100 text-red-800";
      }

      return (
        <span
          className={`px-2 py-1 text-sm font-bold rounded-full ${statusClass}`}
        >
          {status}
        </span>
      );
    },
  },
  {
    id: "actions",
    header: "Actions",
    cell: ({ row }) => {
      const order = row.original;
      const [isDeleteModalOpen, setDeleteModalOpen] = useState(false);
      const [isUpdateModalOpen, setUpdateModalOpen] = useState(false);

      return (
        <>
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button color="black" className="h-8 w-8 p-0">
                <span className="sr-only">Open menu</span>
                <MoreVerticalIcon className="h-4 w-4" />
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
              <DropdownMenuLabel>Actions</DropdownMenuLabel>
              <DropdownMenuItem
                onClick={() => navigator.clipboard.writeText(order.id)}
              >
                Copy ORDER ID
              </DropdownMenuItem>
              <DropdownMenuSeparator />
              <DropdownMenuItem onClick={() => setUpdateModalOpen(true)}>
                Update Order
              </DropdownMenuItem>
              <DropdownMenuItem onClick={() => setDeleteModalOpen(true)}>
                Delete Order
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
          <ConfirmationModal
            orderId={order.id}
            orderDetails={order}
            type="update"
            onSuccess={() => setUpdateModalOpen(false)}
            open={isUpdateModalOpen}
            setOpen={setUpdateModalOpen}
          />
          <ConfirmationModal
            orderId={order.id}
            type="delete"
            onSuccess={() => setDeleteModalOpen(false)}
            open={isDeleteModalOpen}
            setOpen={setDeleteModalOpen}
          />
        </>
      );
    },
  },
];

const OrderTable = () => {
  const { orders } = useOrder();
  const [globalFilter, setGlobalFilter] = useState("");
  const [dateFilter, setDateFilter] = useState("");
  const [salesChannelFilter, setSalesChannelFilter] = useState("");
  const [statusFilter, setStatusFilter] = useState("");

  const handleSearch = (searchTerm: string) => {
    console.log("Search item: ", globalFilter)
    setGlobalFilter(searchTerm);
  };


  return (
    <>
      <div className="flex items-center justify-between mt-4">
        <SearchInput placeholder="Search Order ID" onSearch={handleSearch} />
        <div className="flex items-center space-x-4">
          <Select
            placeholder="Date"
            data={["Today", "Last 7 days", "Last 30 days"]}
            className="w-40"
            onChange={(value) => setDateFilter(value ?? "")} 
          />

          <Select
            placeholder="Sales Channel"
            data={["Online", "Retail", "Wholesale"]}
            className="w-40"
            onChange={(value) => setSalesChannelFilter(value ?? "")}
          />

          <Select
            placeholder="Status"
            data={["PENDING", "DELIVERED"]}
            className="w-40"
            onChange={(value) => setStatusFilter(value ?? "")} 
          />

          <Select
            placeholder="More Filters"
            data={["Discount Applied", "Return Orders"]}
            className="w-40"
          />
        </div>
      </div>
      <DataTable
        columns={columns}
        data={orders ?? []}
        globalFilter={globalFilter}
        dateFilter={dateFilter}
        salesChannelFilter={salesChannelFilter}
        statusFilter={statusFilter}
      />
    </>
  );
};

export default OrderTable;
