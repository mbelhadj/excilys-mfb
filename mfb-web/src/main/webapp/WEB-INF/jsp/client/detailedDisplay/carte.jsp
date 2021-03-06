<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.joda.org/joda/time/tags" prefix="joda"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<header id="overview">
	<%@ include file="../filAriane.jsp"%>

	<div class="row-fluid">
		<div class="span12">
			<h1 align="center">
				<spring:message code="carte.pageTitle" />
			</h1>
			<br />
			<br />

			<p align="right">
				<a href="${contextPath}${urlDetailCompte}" class="btn btn-info"><i
					class="icon-share-alt"></i> <spring:message code="carte.home" /></a>
			</p>
		</div>
	</div>
</header>

<section id="carte">

	<!-- Div comprenant les liens pour changer de mois et afficher le mois courant -->
	<%@ include file="common/monthSelection.jsp"%>

	<!-- Div comprenant le détails des opérations CARTE -->
	<div class="row">
		<div class="span10 offset1">
			<c:if test="${fn:length(operations) ne 0}">
				<table class="table table-striped table-bordered">
					<thead>
						<tr>
							<th class="fixedCol"><spring:message
									code="carte.dateOperation" /></th>
							<th><spring:message code="carte.labelOperation" /></th>
							<th class="colMontant"><spring:message
									code="carte.montantOperation" /></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${operations}" var="o">
							<tr>
								<td><joda:format value="${o.dateValeur}" style="SS" /></td>
								<td>${o.label}</td>
								<c:if test="${o.montant >= 0}">
									<td class="aligneSolde coloreVert">+ <fmt:formatNumber
											value="${o.montant}" minFractionDigits="2" pattern="#,###.##" />
									</td>
								</c:if>
								<c:if test="${o.montant < 0}">
									<td class="aligneSolde coloreRouge">- <fmt:formatNumber
											value="${o.montant*-1}" minFractionDigits="2"
											pattern="#,###.##" />
									</td>
								</c:if>
							</tr>
						</c:forEach>
					</tbody>

				</table>
			</c:if>
			<c:if test="${fn:length(operations) eq 0}">
				<div class="alert alert-info">
					<spring:message code="compte.noOperation" />
				</div>
			</c:if>
		</div>
	</div>

	<!-- Div comprenant la pagination -->
	<%@ include file="common/pageSelection.jsp"%>

</section>