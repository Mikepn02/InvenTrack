import React from "react";
import { FaSearch } from "react-icons/fa";

interface SearchInputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  onSearch?: (searchTerm: string) => void;
  placeholder: string;
  bgColor?: string;
  textColor?: string;
}

const SearchInput: React.FC<SearchInputProps> = ({
  onSearch,
  placeholder,
  bgColor = "bg-gray-100",
  textColor = "text-gray-700",
  ...props
}) => {
  return (
    <div className={`flex items-center px-4 rounded-md border-2 w-full lg:w-96 ${bgColor}`}>
      <FaSearch className={`text-gray-500 mr-2 ${textColor}`} />
      <input className={`w-full focus:outline-none border-none focus:ring-0 bg-transparent px-2 py-1 ${textColor}`} placeholder={placeholder} />
    </div>
  );
};

export default SearchInput;
