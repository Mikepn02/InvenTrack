import React from "react"


interface InputFieldsProps extends React.InputHTMLAttributes<HTMLInputElement>{
    label: string;
}



const InputField = ({ label , ...props} : InputFieldsProps) => {
    return (
        <div className="flex-1">
            <label htmlFor="email" className="block text-sm font-medium leading-6 text-gray-900">
                {label}
            </label> 
            <div className="mt-2">
                <input 
                  className="block w-full rounded-md border-0 py-1.5 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus-ring-2 focus:ring-inset focus:ring-blue-600 sm:text-sm sm:leading-6"
                  {...props}
                />
            </div>
        </div>
    )
}

export default InputField;