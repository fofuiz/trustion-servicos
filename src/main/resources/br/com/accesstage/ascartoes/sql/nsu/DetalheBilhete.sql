SELECT 
	   cia.ticket_number  BILHETE      
      ,cia.agente_iata_code  AGENTE_CODE
      ,cia.boarding_tax_value  TAXA_EMBARQUE
      ,cia.loan_value  ENTRADA
      ,cia.passenger_name NOME_PASSAGEIRO
FROM cia_detailed_ticket CIA 
WHERE COD_ARQUIVO = :codArquivo 
AND cccf_number = :cccfNumber