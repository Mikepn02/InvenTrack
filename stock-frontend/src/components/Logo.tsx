import { CubeIcon } from "@heroicons/react/20/solid";

const Logo = ({ isCollapsed }: { isCollapsed: boolean }) => {
  return (
    <div className="flex items-center">
      <CubeIcon className="h-8 w-8 text-white" />
      {!isCollapsed && (
        <span className="ml-2 text-xl font-bold text-white">Store</span>
      )}
    </div>
  );
};

export default Logo;
