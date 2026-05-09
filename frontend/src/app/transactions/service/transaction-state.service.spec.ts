import { TestBed } from '@angular/core/testing';

import { TransactionStateService } from './transaction-state.services'
import { TransactionService } from "../../shared/services/transaction.service";
import { provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';



describe('TransactionStateService', () => {
  let service: TransactionStateService;
  let transactionServiceSpy: jasmine.SpyObj<TransactionService>

  const testTransaction = {
    id: '123e4567-e89b-12d3-a456-426614174000',
    amount: 150.00,
    description: 'Grocery shopping',
    type: 'EXPENSE',
    date: '2026-04-01',
    categoryName: null,
    createdAt: '2026-04-01T10:00:00'
  }
  const testTransactions = [
    testTransaction,
    {
      id: '223e4567-e89b-12d3-a456-426614174001',
      amount: 3000.00,
      description: 'Monthly salary',
      type: 'INCOME',
      date: '2026-04-01',
      categoryName: null,
      createdAt: '2026-04-01T09:00:00'
    }
  ];

  
    beforeEach(() => {
      const spy = jasmine.createSpyObj('',[
          'getTransactions',
          'createTransaction',
          'updateTransaction',
          'deleteTransaction'
      ])

      TestBed.configureTestingModule({
        providers:[
          TransactionStateService,
          { provide: TransactionService, useValue: spy },
          provideHttpClient()
        ]
      });
      service = TestBed.inject(TransactionStateService);
      transactionServiceSpy = TestBed.inject(TransactionService) as jasmine.SpyObj<TransactionService>
    })

    it('should be created', () => {
      expect(service).toBeTruthy();
    })

    it('should have empty transactions initially',() => {
      expect(service.transactions()).toEqual([])
    });

    it('Should have loading initially', () => {
      expect(service.loading()).toBeFalse();
    })

    it('should have no error initially', () => {
      expect(service.error()).toBe(null)
    })

  it('should load transactions successfully', () => {
    transactionServiceSpy.getTransactions.and.returnValue(of(testTransactions));
    service.getTransactions().subscribe();
    expect(service.transactions()).toEqual(testTransactions)
    expect(service.loading()).toBeFalse()
    expect(service.error()).toBeNull()

  })

  it('Should set loading to true while fetching', () => {
    transactionServiceSpy.getTransactions.and.returnValue(of(testTransactions))

    let loadingDuringCall = false;
     transactionServiceSpy.getTransactions.and.callFake(() => {
      loadingDuringCall = service.loading()
      return of(testTransactions)
     });

     service.getTransactions().subscribe()
     expect(loadingDuringCall).toBeTrue()
     expect(service.loading()).toBeFalse()
  })
  
});
