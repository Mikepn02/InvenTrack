import React, { useState } from "react";
import { StockItem } from "../../types";
import useStock from "../../hooks/useStock";
import { getStockStatus } from "../../lib/utils";
import {
  ColumnDef,
  SortingState,
  useReactTable,
  getCoreRowModel,
  getSortedRowModel,
} from "@tanstack/react-table";
import { DataTable } from "./DataTable";
import { Checkbox } from "../ui/checkbox";
import { ArrowUpDown, MoreVerticalIcon } from "lucide-react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "../ui/dropdown-menu";
import { Button } from "@mantine/core";
import ConsumeItemModal from "../modals/product/ConsumeItem";
import RestockItemModal from "../modals/product/RestockItem";

const getStatusClass = (status: string) => {
  switch (status) {
    case "In Stock":
      return "bg-green-100 text-green-700 px-2 py-1 rounded-full  font-bold";
    case "Low Stock":
      return "bg-yellow-100 text-yellow-700 px-2 py-1 rounded-full  font-bold";
    case "Out of Stock":
      return "bg-red-100 text-red-700 px-2 py-1 rounded-full  font-bold";
    case "Critically Low":
      return "bg-purple-100 text-purple-700 px-2 py-1 rounded-full  font-bold";
    default:
      return "bg-gray-100 text-gray-700 px-2 py-1 rounded-full  font-bold";
  }
};

const columns: ColumnDef<StockItem, any>[] = [
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
    enableSorting: false,
  },
  {
    accessorKey: "name",
    header: ({ column }) => {
      return (
        <button
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
          className="text-center flex"
        >
          <p>Name</p>
          <ArrowUpDown className="ml-2 h-4 w-4" />
        </button>
      );
    },
  },
  {
    accessorKey: "category",
    header: "Category",
  },
  {
    accessorKey: "quantity",
    header: ({ column }) => {
      return (
        <button
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
          className="text-center flex"
        >
          <p>Quantity</p>
          <ArrowUpDown className="ml-2 h-4 w-4" />
        </button>
      );
    },
  },
  {
    accessorKey: "status",
    header: "Status",
    cell: ({ row }) => {
      const status = getStockStatus(row.original.quantity);
      return <span className={getStatusClass(status)}>{status}</span>;
    },
  },
  {
    id: "actions",
    header: "Actions",
    cell: ({ row }) => {
      const item = row.original;
      const [isModalOpen, setModalOpen ] = useState(false);
      const [ isStockModalOpen , setStockModalOpen] = useState(false);

      return (
        <>
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button color="#1f2937" className="h-8 w-8 p-0">
                <span className="sr-only">Open menu</span>
                <MoreVerticalIcon className="h-4 w-4" />
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
              <DropdownMenuLabel>Actions</DropdownMenuLabel>
              <DropdownMenuItem
                onClick={() => navigator.clipboard.writeText(item.id)}
              >
                Copy PRODUCT ID
              </DropdownMenuItem>
              <DropdownMenuItem onClick={() => setModalOpen(true)}>
                Consume Item
              </DropdownMenuItem>
              <DropdownMenuItem onClick={() => setStockModalOpen(true)}>
                Restock Item
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>

          {isModalOpen && (
            <ConsumeItemModal
              onClose={() => setModalOpen(false)}
              productId={item.id}
            />
          )}

          {isStockModalOpen && (
            <RestockItemModal 
              onClose={() => setStockModalOpen(false)}
              productId={item.id}
            />
          )}
        </>
      );
    },
  },
];

const StockTable = () => {
  const { stock, isLoading, error } = useStock();

  return (
    <>
      <DataTable data={stock ?? []} columns={columns} />
    </>
  );
};

export default StockTable;
