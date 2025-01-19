import React from "react";
import { DataTable } from "./DataTable";
import useSupplier from "../../hooks/useSupplier";
import { Checkbox } from "../ui/checkbox";
import { ColumnDef } from "@tanstack/react-table";
import { Supplier } from "../../types";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "../ui/dropdown-menu";
import { Button } from "@mantine/core";
import { MoreVerticalIcon, ChevronDownIcon } from "lucide-react";

const columns: ColumnDef<Supplier>[] = [
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
    accessorKey: "companyName",
    header: "Contact Name",
  },
  {
    accessorKey: "companyHolder",
    header: "Company Holder",
  },
  {
    accessorKey: "companyEmail",
    header: "Company Email",
  },
  {
    accessorKey: "companyPhoneNumber",
    header: "Phone Number",
  },
  {
    accessorKey: "taxId",
    header: "TIN",
  },
  {
    id: "orders",
    header: "Item Names",
    cell: ({ row }) => {
      const item = row.original;

      return (
        <>
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <button>
                <span className="sr-only">Open menu</span>
                <ChevronDownIcon className="h-4 w-4" />
              </button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
              <DropdownMenuLabel>Items Ordered</DropdownMenuLabel>
              {item.itemsNames.map((itemName, index) => (
                <DropdownMenuItem key={index}>{itemName}</DropdownMenuItem>
              ))}
            </DropdownMenuContent>
          </DropdownMenu>
        </>
      );
    },
  },
];
const SupplierTable = () => {
  const { suppliers, isLoading, error } = useSupplier();

  return (
    <>
      <DataTable data={suppliers ?? []} columns={columns} />
    </>
  );
};

export default SupplierTable;
