import {
    ColumnDef,
    ColumnFiltersState,
    flexRender,
    getCoreRowModel,
    getFilteredRowModel,
    getSortedRowModel,
    SortingState,
    useReactTable,
  } from "@tanstack/react-table"
  
   
  import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
  } from "../ui/table"
import EmptyView from "../EmptyView"
import { useEffect, useState } from "react"


interface DataTableProps<TData, TValue> {
    columns: ColumnDef<TData, TValue>[]
    data: TData[]
}
   

export function DataTable<TData, TValue>({
  columns,
  data,
  globalFilter,
  dateFilter,
  salesChannelFilter,
  statusFilter
}: DataTableProps<TData, TValue> & {
  globalFilter?: string;
  dateFilter?: string;
  salesChannelFilter?: string;
  statusFilter?: string;
}) {
  const [sorting, setSorting] = useState<SortingState>([]);
  const [columnFilters, setColumnFilters] = useState<ColumnFiltersState>([]);

  const globalFilterFn = (row: any, columnId: any, filterValue: any) => {
    const value = row.getValue(columnId);
    return String(value).toLowerCase().includes(String(filterValue).toLowerCase());
  };
  const table = useReactTable({
    data,
    columns,
    onSortingChange: setSorting,
    onColumnFiltersChange: setColumnFilters,
    getCoreRowModel: getCoreRowModel(),
    getSortedRowModel: getSortedRowModel(),
    getFilteredRowModel: getFilteredRowModel(),
    globalFilterFn,
    state: {
      sorting,
      columnFilters,
      globalFilter
    },
  });

  

  
  useEffect(() => {
    const filters = [
      { id: 'date', value: dateFilter },
      { id: 'salesChannel', value: salesChannelFilter },
      { id: 'status', value: statusFilter },
    ].filter(filter => filter.value); 
    setColumnFilters(filters);
  }, [dateFilter, salesChannelFilter, statusFilter]); 
  return (
    <div className="overflow-x-auto mt-2 rounded-t-md space-y-4 bg-gray-50">
      <Table className="w-full min-w-[600px]">
        <TableHeader>
          {table.getHeaderGroups().map((headerGroup) => (
            <TableRow key={headerGroup.id}>
              {headerGroup.headers.map((header) => {
                return (
                  <TableHead key={header.id}>
                    {header.isPlaceholder
                      ? null
                      : flexRender(
                          header.column.columnDef.header,
                          header.getContext()
                        )}
                  </TableHead>
                );
              })}
            </TableRow>
          ))}
        </TableHeader>
        <TableBody>
          {table.getRowModel().rows?.length ? (
            table.getRowModel().rows.map((row) => (
              <TableRow
                key={row.id}
                data-state={row.getIsSelected() && "selected"}
              >
                {row.getVisibleCells().map((cell) => (
                  <TableCell key={cell.id} className="h-20">
                    {flexRender(cell.column.columnDef.cell, cell.getContext())}
                  </TableCell>
                ))}
              </TableRow>
            ))
          ) : (
            <TableRow>
              <TableCell colSpan={columns.length} className="h-32 text-center">
                <EmptyView message="No Data To Show"/>
              </TableCell>
            </TableRow>
          )}
        </TableBody>
      </Table>
    </div>
  );
}



