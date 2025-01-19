
import useAuth from "../../hooks/useAuth";
import { getDate, getGreeting } from "../../lib/utils";
import StatCard from "../../components/cards/StatCard";
import StockTable from "../../components/tables/StockTable";
import { Bar, Doughnut } from "react-chartjs-2";
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement } from "chart.js";
import useStock from "../../hooks/useStock";
import useOrder from "../../hooks/useOrder";

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement);

const Overview = () => {
  const { user } = useAuth();
  const { stock } = useStock();
  const { orders } = useOrder();
  const greeting = getGreeting();
  const currentDate = getDate();

  const productNames = stock?.map((item) => item.name);
  const quantities = stock?.map((item) => item.quantity);
  const consumedQuantities = stock?.map((item) => item.consumedQuantity);
  const availableItems = stock?.map((item) => item.quantity - item.consumedQuantity);

 
  const barData = {
    labels: productNames,
    datasets: [
      {
        label: "Available Stock",
        data: quantities,
        backgroundColor: "#111827",
      },
    ],
  };


  const doughnutData = {
    labels: productNames,
    datasets: [
      {
        label: "Consumed Stock",
        data: consumedQuantities,
        backgroundColor: ["#111827", "#FF6384", "#FFCE56", "#4BC0C0", "#9966FF"],
        hoverOffset: 4,
      },
    ],
  };

  return (
    <section className="w-full flex flex-col gap-y-6">
      <div className="flex flex-col md:flex-row items-center justify-between space-y-4 md:space-y-0">
        <div>
          <h1 className="text-2xl md:text-3xl font-extrabold text-center md:text-left">
            {greeting}, {user?.firstName || "User"}
          </h1>
          <p className="text-base text-gray-500">Welcome back to the stock management system</p>
        </div>

        <div className="text-base md:text-lg text-gray-500">
          Today is {currentDate}
        </div>
      </div>

      <div className="w-full grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-4">
        <StatCard
          title="Total Orders"
          value={orders?.length || 0}
          quantity="sent"
          bgColor="bg-[#F0F8FF]"
        />
        <StatCard
          title="Total Stock Items"
          value={stock?.length || 0}
          quantity="items"
          bgColor="bg-[#F1F2F7]"
        />
        <StatCard
          title="Stock Left"
          value={availableItems?.length || 0}
          quantity="Items"
          bgColor="bg-[#F0F8FF]"
        />
        <StatCard
          title="Running Out Items"
          value={0}
          quantity="Items"
          bgColor="bg-[#F1F2F7]"
        />
      </div>
      <div className="w-full grid grid-cols-1 gap-6 md:grid-cols-2">
        <div style={{ height: "500px" }}>
          <h2 className="text-lg font-bold mb-4">Daily Uses</h2>
          <Bar data={barData} options={{ responsive: true, maintainAspectRatio: false }} />
        </div>

        <div style={{ height: "500px" }}>
          <h2 className="text-lg font-bold mb-4">Stock Status</h2>
          <Doughnut data={doughnutData} options={{ responsive: true, maintainAspectRatio: false }} />
        </div>
      </div>

      <h1 className="text-xl font-bold md:text-3xl sm:text-2xl mt-10">Stock Items List</h1>
      <div className="mt-4">
        <StockTable />
      </div>
    </section>
  );
};

export default Overview;
