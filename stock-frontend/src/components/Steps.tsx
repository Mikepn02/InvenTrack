import React, { useState } from "react";
import { Button } from "@mantine/core";

interface StepMessageProps {
  step: number;
  children: React.ReactNode;
}

const StepMessage = ({ step, children }: StepMessageProps) => (
  <div className="text-xl my-10 font-bold flex-1 flex-col">
    {children}
  </div>
);

interface StepsProps {
  stepsContent: React.ReactNode[];
}

const Steps = ({ stepsContent }: StepsProps) => {
  const [step, setStep] = useState(1);
  const [isOpen, setIsOpen] = useState(true);

  const handlePrevious = () => {
    if (step > 1) setStep((s) => s - 1);
  };

  const handleNext = () => {
    if (step < stepsContent.length) setStep((s) => s + 1);
  };

  return (
    <div>
      <button
        className="absolute top-4 right-4 border-none bg-transparent cursor-pointer text-5xl text-current hover:text-indigo-600"
        onClick={() => setIsOpen((is) => !is)}
      >
        &times;
      </button>

      {isOpen && (
        <div className="w-full bg-gray-100 rounded-lg p-6 md:p-16 mx-auto mt-24">
          <div className="flex justify-between">
            {stepsContent.map((_, index) => (
              <div
                key={index}
                className={`h-10 w-10 bg-gray-300 rounded-full flex items-center justify-center text-lg ${
                  step >= index + 1 ? "bg-indigo-600 text-white" : ""
                }`}
              >
                {index + 1}
              </div>
            ))}
          </div>

          <StepMessage step={step}>{stepsContent[step - 1]}</StepMessage>

          <div className="flex justify-between">
            <Button onClick={handlePrevious} disabled={step === 1}>
              <span>👈</span> Previous
            </Button>
            <Button onClick={handleNext} disabled={step === stepsContent.length}>
              Next <span>👉</span> <span>🤓</span>
            </Button>
          </div>
        </div>
      )}
    </div>
  );
};

export default Steps;
