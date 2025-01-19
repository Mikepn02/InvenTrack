import React from 'react';

const StatCard = ({ title, value, quantity, bgColor }: { title: string; value: number; quantity: string | number; bgColor: string }) => {
  return (
    <div className={`w-full p-6 flex flex-col items-end gap-y-3 shadow-md rounded-md border text-blue-900 ${bgColor}`}>
      <h3 className='text-xl font-extrabold'>{title}</h3>
      <p className='text-3xl font-bold'>{value}</p>
    </div>
  );
};

export default StatCard;
