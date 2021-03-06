package com.ebi.formation.mfb.webservices.jaxrs.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ebi.formation.mfb.servicesapi.IOperationService;
import com.ebi.formation.mfb.servicesapi.IOperationService.ReturnCodeOperation;
import com.ebi.formation.mfb.webservicesapi.dto.IntegrationOperationDTO;
import com.ebi.formation.mfb.webservicesapi.dto.OperationDTO;
import com.ebi.formation.mfb.webservicesapi.dto.converters.OperationConverter;
import com.ebi.formation.mfb.webservicesapi.jaxrs.IOperationWebService;

/**
 * Implémentation de {@link IOperationWebService}
 * 
 * @author excilys
 * 
 */
public class OperationWebService implements IOperationWebService {

	@Autowired
	private IOperationService operationService;

	/*
	 * (non-Javadoc)
	 * @see com.ebi.formation.mfb.webservicesapi.jaxrs.IOperationWebService#getLastOperationsByCompteId(java.lang.Long,
	 * int)
	 */
	@Override
	public List<OperationDTO> getLastOperationsByCompteId(Long compteId, int numberOfOperations) {
		return OperationConverter.convertOperationListToOperationDTOList(operationService.getLastOperationByCompte(
				compteId, numberOfOperations));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.ebi.formation.mfb.webservicesapi.jaxrs.IOperationWebService#addNewOperation(com.ebi.formation.mfb.webservicesapi
	 * .dto.IntegrationOperationDTO)
	 */
	@Override
	public Boolean addNewOperation(IntegrationOperationDTO integrationOperationDTO) {
		ReturnCodeOperation returnedCode = operationService.saveOperation(integrationOperationDTO.getMontant(),
				integrationOperationDTO.getNumeroCompte(), integrationOperationDTO.getType(),
				integrationOperationDTO.getLabel(), integrationOperationDTO.getDateEffet(),
				integrationOperationDTO.getDateValeur());
		if (returnedCode.equals(ReturnCodeOperation.OK)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}
