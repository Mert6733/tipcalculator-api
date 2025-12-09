declare module '@apiverve/tipcalculator' {
  export interface tipcalculatorOptions {
    api_key: string;
    secure?: boolean;
  }

  export interface tipcalculatorResponse {
    status: string;
    error: string | null;
    data: TipCalculatorData;
    code?: number;
  }


  interface TipCalculatorData {
      billAmount:       number;
      tipPercentage:    number;
      tipAmount:        number;
      totalAmount:      number;
      currency:         string;
      splitBetween:     number;
      perPerson:        PerPerson;
      commonTipAmounts: CommonTipAmount[];
  }
  
  interface CommonTipAmount {
      percentage: number;
      tipAmount:  number;
      total:      number;
      perPerson:  number;
  }
  
  interface PerPerson {
      billAmount:  number;
      tipAmount:   number;
      totalAmount: number;
  }

  export default class tipcalculatorWrapper {
    constructor(options: tipcalculatorOptions);

    execute(callback: (error: any, data: tipcalculatorResponse | null) => void): Promise<tipcalculatorResponse>;
    execute(query: Record<string, any>, callback: (error: any, data: tipcalculatorResponse | null) => void): Promise<tipcalculatorResponse>;
    execute(query?: Record<string, any>): Promise<tipcalculatorResponse>;
  }
}
