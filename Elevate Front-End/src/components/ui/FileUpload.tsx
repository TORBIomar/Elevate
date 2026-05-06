import { useRef, useState } from 'react';
import { UploadCloud, FileText, X } from 'lucide-react';

interface FileUploadProps {
  label: string;
  accept?: string;
  required?: boolean;
  value: File | null;
  onChange: (file: File | null) => void;
  helperText?: string;
}

export default function FileUpload({ 
  label, 
  accept = '.pdf,.doc,.docx', 
  required = false, 
  value, 
  onChange,
  helperText
}: FileUploadProps) {
  const inputRef = useRef<HTMLInputElement>(null);
  const [isDragging, setIsDragging] = useState(false);

  const handleFile = (file: File | undefined | null) => {
    if (file) {
      onChange(file);
    }
  };

  const onDragOver = (e: React.DragEvent) => {
    e.preventDefault();
    setIsDragging(true);
  };

  const onDragLeave = (e: React.DragEvent) => {
    e.preventDefault();
    setIsDragging(false);
  };

  const onDrop = (e: React.DragEvent) => {
    e.preventDefault();
    setIsDragging(false);
    if (e.dataTransfer.files && e.dataTransfer.files.length > 0) {
      handleFile(e.dataTransfer.files[0]);
      e.dataTransfer.clearData();
    }
  };

  return (
    <div className="space-y-1">
      <label className="block text-sm font-medium text-surface-200">
        {label} {required && <span className="text-red-400">*</span>}
      </label>
      
      {!value ? (
        <div
          className={`relative flex justify-center px-6 pt-5 pb-6 border-2 border-dashed rounded-xl transition-colors cursor-pointer ${
            isDragging 
              ? 'border-primary-500 bg-primary-500/10' 
              : 'border-surface-700 bg-surface-800/30 hover:bg-surface-800 hover:border-surface-600'
          }`}
          onDragOver={onDragOver}
          onDragLeave={onDragLeave}
          onDrop={onDrop}
          onClick={() => inputRef.current?.click()}
        >
          <div className="space-y-1 text-center">
            <UploadCloud className="mx-auto h-12 w-12 text-surface-400" />
            <div className="flex text-sm text-surface-300">
              <span className="relative rounded-md font-medium text-primary-400 focus-within:outline-none focus-within:ring-2 focus-within:ring-offset-2 focus-within:ring-primary-500">
                <span>Upload a file</span>
              </span>
              <p className="pl-1">or drag and drop</p>
            </div>
            <p className="text-xs text-surface-500">
              {helperText || `Supported formats: ${accept}`}
            </p>
          </div>
          <input
            ref={inputRef}
            type="file"
            className="sr-only"
            accept={accept}
            onChange={(e) => handleFile(e.target.files?.[0])}
            required={required}
          />
        </div>
      ) : (
        <div className="flex items-center justify-between p-4 bg-surface-800 border border-surface-700 rounded-xl">
          <div className="flex items-center gap-3 overflow-hidden">
            <div className="w-10 h-10 shrink-0 rounded-lg bg-primary-500/10 text-primary-400 flex items-center justify-center">
              <FileText className="w-5 h-5" />
            </div>
            <div className="min-w-0">
              <p className="text-sm font-medium text-surface-100 truncate">
                {value.name}
              </p>
              <p className="text-xs text-surface-400">
                {(value.size / 1024 / 1024).toFixed(2)} MB
              </p>
            </div>
          </div>
          <button
            type="button"
            className="p-2 text-surface-400 hover:text-red-400 transition-colors"
            onClick={(e) => {
              e.stopPropagation();
              onChange(null);
              if (inputRef.current) inputRef.current.value = '';
            }}
          >
            <X className="w-5 h-5" />
          </button>
        </div>
      )}
    </div>
  );
}
