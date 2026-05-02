import { useFxTicker } from '../hooks/useFxTicker.js';

export default function FXTicker() {
  const rates = useFxTicker();
  return (
    <div className="flex max-w-full flex-wrap gap-1.5 text-xs">
      {Object.entries(rates).filter(([code]) => code !== 'INR').map(([code, rate]) => (
        <span key={code} className="inline-flex items-center gap-1 rounded-md border border-emerald-100 bg-emerald-50 px-2 py-1 font-medium text-teal">
          <span className="h-1.5 w-1.5 rounded-full bg-teal shadow-[0_0_0_3px_rgba(8,127,123,0.12)]" />
          INR/{code} {Number(rate).toFixed(4)}
        </span>
      ))}
    </div>
  );
}
